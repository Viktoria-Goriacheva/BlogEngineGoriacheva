package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
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
