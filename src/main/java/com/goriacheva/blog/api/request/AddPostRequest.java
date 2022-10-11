package com.goriacheva.blog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddPostRequest {

  private long time;
  @JsonProperty("active")
  private byte active;
  private String title;
  private List<String> tags;
  private String text;
}
