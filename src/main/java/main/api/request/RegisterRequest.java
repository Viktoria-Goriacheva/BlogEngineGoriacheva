package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.api.annotations.Email;
import main.api.annotations.Name;
import main.api.annotations.Password;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class RegisterRequest {
  @Email
  @JsonProperty("e_mail")
  private String eMail;
  @Password
  private String password;
  @Name
  private String name;
  private String captcha;
  @JsonProperty("captcha_secret")
  private String captchaSecret;

}
