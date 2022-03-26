package main.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class Key implements Serializable {

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post postId;
  @ManyToOne
  @JoinColumn(name = "tag_id")
  private Tag tagId;

  public Key() {
  }

  public Post getPost() {
    return postId;
  }

  public void setPost(Post post) {
    this.postId = post;
  }

  public Tag getTag() {
    return tagId;
  }

  public void setTag(Tag tag) {
    this.tagId = tag;
  }
}
