package com.goriacheva.blog.api.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.goriacheva.blog.dto.CommentDto;
import com.goriacheva.blog.dto.UserDtoForPost;

@Builder
@Getter
@Setter
public class PostIdResponse {
  private Integer id;
  private Long timestamp;
  private boolean active;
  private UserDtoForPost user;
  private String title;
  private String text;
  private Integer likeCount;
  private Integer dislikeCount;
  private Integer viewCount;
  private List<CommentDto> comments;
  private List<String> tags;

}
