package com.goriacheva.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.goriacheva.blog.annotations.Email;
import com.goriacheva.blog.annotations.Name;
import com.goriacheva.blog.annotations.Password;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
