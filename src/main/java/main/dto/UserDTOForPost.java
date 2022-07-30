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
    "name"
})
public class UserDTOForPost {

  private Integer id;
  private String name;

  public UserDTOForPost() {
  }

  public UserDTOForPost(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
}
