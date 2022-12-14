package com.goriacheva.blog.model;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.goriacheva.blog.repository.Role;
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
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

  public Role getRole() {
    return isModerator == 1 ? Role.MODERATOR : Role.USER;
  }

}
