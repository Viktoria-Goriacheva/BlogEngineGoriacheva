package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import main.dto.UserDTOForCheck;
import org.springframework.stereotype.Component;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class AuthCheckResponse {

  private boolean result;
  private UserDTOForCheck user;

  public AuthCheckResponse() {
    user = new UserDTOForCheck();
  }

  public AuthCheckResponse(boolean result) {
    this.result = result;
    if (!result) {
      this.user = null;
    }
  }
}
