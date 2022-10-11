package com.goriacheva.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaResponse {

  @JsonProperty("secret")
  private String secretCode;

  @JsonProperty("image")
  private String imageBase64;
}