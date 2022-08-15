package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.annotations.Email;
import main.annotations.Name;
import main.annotations.Password;
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
