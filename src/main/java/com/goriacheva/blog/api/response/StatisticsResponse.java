package com.goriacheva.blog.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StatisticsResponse {

  private Integer postsCount;
  private Integer likesCount;
  private Integer dislikesCount;
  private Integer viewsCount;
  private long firstPublication;
}
