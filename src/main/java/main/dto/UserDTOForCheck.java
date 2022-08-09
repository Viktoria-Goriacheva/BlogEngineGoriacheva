package main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTOForCheck {

  private int id;
  private String name;
  private String photo;
  private String email;
  private boolean moderation;
  private int moderationCount;
  private boolean settings;

}
