package main.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.ToString;

@ToString
@Entity
@Table(name = "users")
public class User {

  @Id
  @NotNull
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "is_moderator", nullable = false)
  private byte isModerator;
  @Column(name = "reg_time", nullable = false)
  private LocalDateTime regTime;
  @NotNull
  private String name;
  @NotNull
  private String email;
  @NotNull
  private String password;
  private String code;
  private String photo;
  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
  private List<Post> posts;
  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
  private List<PostVote> postVotes;
  @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<PostComment> comments;

  public User() {
  }

  public List<PostComment> getComments() {
    return comments;
  }

  public void setComments(List<PostComment> comments) {
    this.comments = comments;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public byte getIsModerator() {
    return isModerator;
  }

  public void setIsModerator(byte isModerator) {
    this.isModerator = isModerator;
  }

  public LocalDateTime getRegTime() {
    return regTime;
  }

  public void setRegTime(LocalDateTime regTime) {
    this.regTime = regTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  public List<PostVote> getPostVotes() {
    return postVotes;
  }

  public void setPostVotes(List<PostVote> postVotes) {
    this.postVotes = postVotes;
  }
}
