package com.goriacheva.blog.model;

import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull
  @Column(name = "is_active")
  private byte isActive;
  @Column(name = "moderation_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private ModerationStatus moderationStatus;
  @Column(name = "moderator_id")
  private Integer moderatorId;
  @NotNull
  private LocalDateTime time;
  @NotNull
  private String title;
  @NotNull
  private String text;
  @Column(name = "view_count", nullable = false)
  private int viewCount;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "user_id")
  private User user;
  @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
  private List<PostVote> postVotes;
  @ManyToMany
  @JoinTable(name = "tag2post",
      joinColumns = {@JoinColumn(name = "post_id")},
      inverseJoinColumns = {@JoinColumn(name = "tag_id")})
  private List<Tag> tags;
  @OneToMany(mappedBy = "post")
  private List<PostComment> comments;

}

