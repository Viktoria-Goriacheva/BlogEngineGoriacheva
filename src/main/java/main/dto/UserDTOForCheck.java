package main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "photo",
    "email",
    "moderation",
    "moderationCount",
    "settings"
})
public class UserDTOForCheck {

  public int id;
  public String name;
  public String photo;
  public String email;
  public boolean moderation;
  public int moderationCount;
  public boolean settings;

}
