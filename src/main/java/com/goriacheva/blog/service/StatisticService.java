package com.goriacheva.blog.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.goriacheva.blog.api.response.StatisticsResponse;
import com.goriacheva.blog.model.Post;
import com.goriacheva.blog.model.PostVote;
import com.goriacheva.blog.model.User;
import com.goriacheva.blog.repository.PostRepository;
import com.goriacheva.blog.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;

  public StatisticsResponse getStatistic() {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findUserByEmail(userEmail);
    List<Post> list = postRepository.findPostByIdUser(user.getId());
    int like = 0;
    int dislike = 0;
    int postsCount = list.size();
    int viewsCount = 0;
    LocalDateTime firstPublication = postRepository.findFirstPostByIdUser(user.getId());
    Duration duration = Duration.between(firstPublication, LocalDateTime.now());
    long secondsFirstPost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
    for (Post post : list) {
      viewsCount = viewsCount + post.getViewCount();
      for (PostVote postVote : post.getPostVotes()) {
        if (postVote.getValue() == 1) {
          like++;
        } else {
          dislike++;
        }
      }
    }
    return buildResultStatistics(like, dislike, postsCount, secondsFirstPost, viewsCount);
  }

  public StatisticsResponse getStatisticAll() {
    List<Post> list = postRepository.findAllPost();
    int like = 0;
    int dislike = 0;
    int postsCount = list.size();
    int viewsCount = 0;
    LocalDateTime firstPublication = postRepository.findFirstPostInBlog();
    Duration duration = Duration.between(firstPublication, LocalDateTime.now());
    long secondsFirstPost = (System.currentTimeMillis() / 1000L) - duration.getSeconds();
    for (Post post : list) {
      viewsCount = viewsCount + post.getViewCount();
      for (PostVote postVote : post.getPostVotes()) {
        if (postVote.getValue() == 1) {
          like++;
        } else {
          dislike++;
        }
      }
    }
    return buildResultStatistics(like, dislike, postsCount, secondsFirstPost, viewsCount);
  }

  private StatisticsResponse buildResultStatistics(int like, int dislike, int postsCount,
      long firstPublication, int viewsCount) {
    StatisticsResponse response = StatisticsResponse.builder()
        .likesCount(like)
        .dislikesCount(dislike)
        .postsCount(postsCount)
        .firstPublication(firstPublication)
        .viewsCount(viewsCount)
        .build();
    return response;
  }
}