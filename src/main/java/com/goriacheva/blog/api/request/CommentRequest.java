package com.goriacheva.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentRequest {

  @JsonProperty("parent_id")
  private Integer parentId;
  @JsonProperty("post_id")
  private Integer postId;
  private String text;
}
