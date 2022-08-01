package main.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jsoup.Jsoup;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {

  private Integer id;
  private Long timestamp;
  private UserDTOForPost user;
  private String title;
  private String announce;
  private Integer likeCount;
  private Integer dislikeCount;
  private Integer commentCount;
  private Integer viewCount;

}

