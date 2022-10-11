package com.goriacheva.blog.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goriacheva.blog.annotations.Email;
import com.goriacheva.blog.annotations.Name;
import com.goriacheva.blog.annotations.Password;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileRequest {

  @Name
  private final String name;
  @Email
  private final String email;
  @Password
  private final String password;
  private final int removePhoto;
}
