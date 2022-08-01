package main.api.response;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import main.dto.TagDTO;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class TagResponse {

  private List<TagDTO> tags;

}
