package main.api.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {
  private boolean result;
  private Map<String, String> errors;
  private Integer id;
}
