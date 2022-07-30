package main.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import main.api.response.TagResponse;
import main.dto.TagDTO;
import main.mappers.MapperDTO;
import main.model.Tag;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
@EqualsAndHashCode
@RequiredArgsConstructor
public class TagService {

  private final MapperDTO mapperDTO;
  private final TagRepository tagRepository;
  private final PostRepository postRepository;

  public TagResponse tag(String query) {
    TagResponse tagResponse = new TagResponse();
    List<Tag> tagList = findTagsWithQuery(query);
    List<TagDTO> tagDTOList = prepareTag(tagList);
    tagResponse.setTags(tagDTOList);
    return tagResponse;
  }

  private List<TagDTO> prepareTag(List<Tag> tagList) {
    List<TagDTO> result = new ArrayList<>();
    Integer mostPopularTagWeight = findMostPopularTagWeight(tagList);
    double factor = calculateFactor(mostPopularTagWeight);
    for (Tag tag : tagList) {
      TagDTO tagDTO = mapperDTO.TagToTagDto(tag);
      tagDTO.setWeight(calculateTagWeight(tagList.size(), factor));
      result.add(tagDTO);
    }
    return result;
  }

  private List<Tag> findTagsWithQuery(String query) {
    if (query.isEmpty()) {
      return tagRepository.findAll();
    }
    return tagRepository.findAllByName(query);
  }

  private Integer findMostPopularTagWeight(List<Tag> tagList) {
    HashMap<String, Integer> namedTags = new HashMap<>();
    for (Tag tagName : tagList) {
      namedTags.put(tagName.getName(), Collections.frequency(tagList, tagName.getName()));
    }
    Integer maxValue = Collections.max(namedTags.values());
    return maxValue;
  }

  private double calculateTagWeight(int singleTag, double factor) {
    double dWeightTag = (double) singleTag / postRepository.findAll().size();
    return dWeightTag * factor;
  }

  private double calculateFactor(int mostPopularTagWeight) {
    double dWeightMax = (double) mostPopularTagWeight / postRepository.findAll().size();
    return 1 / dWeightMax;
  }
}
