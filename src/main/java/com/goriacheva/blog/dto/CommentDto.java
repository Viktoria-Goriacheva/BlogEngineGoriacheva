package com.goriacheva.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

  private Integer id;
  private Long timestamp;
  private String text;
  private UserDtoForPostId user;
}
