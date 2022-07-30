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
@ToString
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "timestamp",
    "user",
    "title",
    "announce",
    "likeCount",
    "dislikeCount",
    "commentCount",
    "viewCount"
})
public class PostDTO {

  public Integer id;
  public Long timestamp;
  public UserDTOForPost user;
  public String title;
  public String announce;
  public Integer likeCount;
  public Integer dislikeCount;
  public Integer commentCount;
  public Integer viewCount;

}

