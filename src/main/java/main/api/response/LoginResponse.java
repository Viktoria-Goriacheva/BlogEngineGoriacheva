package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.UserDTO;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

  private boolean result;
  private UserDTO user;
}
