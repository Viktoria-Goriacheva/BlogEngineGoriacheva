package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class AuthCheckResponse {

  private boolean result;

  private UserResponse user;

  public AuthCheckResponse() {
    user = new UserResponse();
  }

  public AuthCheckResponse(boolean result) {
    this.result = result;
    if (!result) {
      this.user = null;
    }
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public UserResponse getUser() {
    return user;
  }

  public void setUser(UserResponse user) {
    this.user = user;
  }
}
