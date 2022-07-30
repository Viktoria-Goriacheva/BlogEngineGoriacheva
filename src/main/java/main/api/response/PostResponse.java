package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.PostDTO;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "count",
    "posts"
})
public class PostResponse {

  public Integer count;
  @JsonProperty("posts")
  public List<PostDTO> postsDTO;
}
