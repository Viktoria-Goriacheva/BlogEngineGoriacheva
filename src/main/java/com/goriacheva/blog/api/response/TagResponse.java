package com.goriacheva.blog.api.response;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.goriacheva.blog.dto.TagDto;

@NoArgsConstructor
@Getter
@Setter
public class TagResponse {

  private List<TagDto> tags;

  public List<TagDto> getSort() {
    Collections.sort(tags, Comparator.comparingDouble(TagDto::getWeight));
    return tags;
  }
}
