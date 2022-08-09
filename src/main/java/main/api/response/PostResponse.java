package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.PostDTO;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {

  private Integer count;
  @JsonProperty("posts")
  private List<PostDTO> postsDTO;
}
