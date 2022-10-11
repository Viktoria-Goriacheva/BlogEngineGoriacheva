package com.goriacheva.blog.model;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "post_comments")
public class PostComment {
  @Id
  @NotNull
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @NotNull
  private LocalDateTime regTime;
  @NotNull
  private String text;
  @ManyToOne
  @JoinColumn(name = "parent_id")
  private PostComment parent;
  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  private Post post;
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
}
