package com.goriacheva.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goriacheva.blog.annotations.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {

  private String code;
  @Password
  private String password;
  private String captcha;
  @JsonProperty("captcha_secret")
  private String captchaSecret;
}
