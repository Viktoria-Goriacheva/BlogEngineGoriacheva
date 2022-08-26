package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponse {

  private boolean result;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, String> errors;

}
