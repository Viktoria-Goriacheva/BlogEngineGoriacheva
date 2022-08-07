package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.CommentDTO;
import main.dto.UserDTOForPost;
import main.dto.UserDTOForPostId;
import main.model.Tag;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostIdResponse {
  private Integer id;
  private Long timestamp;
  private boolean active;
  private UserDTOForPost user;
  private String title;
  private String text;
  private Integer likeCount;
  private Integer dislikeCount;
  private Integer viewCount;
  @JsonProperty("comments")
  private List<CommentDTO> comments;
  private List<String> tags;



}
