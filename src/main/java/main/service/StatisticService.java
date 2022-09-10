package main.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import main.api.response.StatisticsResponse;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.repository.PostRepository;
import main.repository.UserRepository;
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
    //  STATISTICS_IS_PUBLIC если включен этот режим, статистика блога должна быть доступна по запросу
    //   GET /api/statistics/all для всех групп пользователей. Если режим выключен, по запросу GET
// /api/statistics/all только модераторам отдавать данные статистики. Пользователям и гостям блога
    //   необходимо возвращать статус 401
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