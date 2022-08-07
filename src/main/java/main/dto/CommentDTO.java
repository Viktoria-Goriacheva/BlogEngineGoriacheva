package main.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {

  private Integer id;
  private Long timestamp;
  private String text;
  private UserDTOForPostId user;
  private List<CommentDTO> listOfParent = new ArrayList<>();
}
