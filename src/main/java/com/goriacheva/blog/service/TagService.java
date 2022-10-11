package com.goriacheva.blog.service;

import com.goriacheva.blog.api.response.TagResponse;
import com.goriacheva.blog.dto.TagDto;
import com.goriacheva.blog.repository.PostRepository;
import com.goriacheva.blog.repository.TagRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
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
    List<TagDto> tagDtoList = prepareTag(tagList);
    tagResponse.setTags(tagDtoList);
    tagResponse.getSort();
    return tagResponse;
  }

  private List<TagDto> prepareTag(List<String> tagList) {
    List<TagDto> result = new ArrayList<>();
    String mostPopularTag = tagRepository.findPopularTags();
    Integer countPopularTag = Integer.parseInt(mostPopularTag.split(",")[0]);
    double factor = calculateFactor(countPopularTag);
    for (String tag : tagList) {
      TagDto tagDTO = new TagDto();
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
