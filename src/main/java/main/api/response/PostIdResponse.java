package main.api.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.dto.CommentDTO;
import main.dto.UserDTOForPost;
import org.springframework.stereotype.Component;

@Builder
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
  private List<CommentDTO> comments;
  private List<String> tags;

}
