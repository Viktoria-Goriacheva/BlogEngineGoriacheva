package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Builder
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsResponse {

  private Integer postsCount;
  private Integer likesCount;
  private Integer dislikesCount;
  private Integer viewsCount;
  private long firstPublication;
}
