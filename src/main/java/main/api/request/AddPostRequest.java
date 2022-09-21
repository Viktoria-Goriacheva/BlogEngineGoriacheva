package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AddPostRequest {

  private long time;
  @JsonProperty("active")
  private byte active;
  private String title;
  private List<String> tags;
  private String text;
}
