package main.api.response;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.TagDTO;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class TagResponse {

  private List<TagDTO> tags;

  public List<TagDTO> getSort() {
    Collections.sort(tags, Comparator.comparingDouble(TagDTO::getWeight));
    return tags;
  }
}
