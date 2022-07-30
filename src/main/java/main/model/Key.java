package main.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class Key implements Serializable {

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post postId;
  @ManyToOne
  @JoinColumn(name = "tag_id")
  private Tag tagId;

}
