package main.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import main.annotations.Email;
import main.annotations.Name;
import main.annotations.Password;

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
