package main.service;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import main.api.response.TagResponse;
import main.dto.TagDTO;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
@EqualsAndHashCode
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;
  private final PostRepository postRepository;

  public TagResponse tag(String query) {
    TagResponse tagResponse = new TagResponse();
    List<String> tagList = findTagsWithQuery(query);
    List<TagDTO> tagDTOList = prepareTag(tagList);
    tagResponse.setTags(tagDTOList);
    tagResponse.getSort();
    return tagResponse;
  }

  private List<TagDTO> prepareTag(List<String> tagList) {
    List<TagDTO> result = new ArrayList<>();
    String mostPopularTag = tagRepository.findPopularTags();
    Integer countPopularTag = Integer.parseInt(mostPopularTag.split(",")[0]);
    double factor = calculateFactor(countPopularTag);
    for (String tag : tagList) {
      TagDTO tagDTO = new TagDTO();
      tagDTO.setName(tag);
      tagDTO.setWeight(calculateTagWeight(tagRepository.findCountOneTags(tag), factor));
      result.add(tagDTO);
    }
    return result;
  }

  private List<String> findTagsWithQuery(String query) {
    if (query.isEmpty()) {
      return tagRepository.findAllTags();
    }
    return tagRepository.findAllByName(query);
  }

  private double calculateTagWeight(int singleTag, double factor) {
    double dWeightTag = (double) singleTag / postRepository.findAllPost().size();
    return dWeightTag * factor;
  }

  private double calculateFactor(Integer countPopularTag) {
    double dWeightMax = (double) countPopularTag / postRepository.findAllPost().size();
    return 1 / dWeightMax;
  }
}
