package main.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import main.api.response.ModerationResponse;
import main.model.Post;
import main.model.PostVote;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import main.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

  private byte dislike = -1;
  private byte like = 1;
  private final PostVoteRepository postVoteRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  public ModerationResponse setLike(Integer postId) {
    ModerationResponse response = setDislikeOrLike(postId, like, dislike);
    return response;
  }

  public ModerationResponse setDislike(Integer postId) {
    ModerationResponse response = setDislikeOrLike(postId, dislike, like);
    return response;
  }

  private ModerationResponse setDislikeOrLike(Integer postId, byte dislike, byte like) {
    ModerationResponse response = new ModerationResponse();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)
        && authentication.isAuthenticated()) {
      String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();
      Integer userId = userRepository.findByIdUserForMod(emailUser);
      Optional<PostVote> postVoteOptional = postVoteRepository.findOptionalVoteByIdUserAndIdPost(
          userId, postId);
      if (!postVoteOptional.isEmpty()) {
        if (postVoteOptional.get().getValue() == like) {
          PostVote p = postVoteOptional.get();
          p.setValue(dislike);
          p.setTime(LocalDateTime.now());
          Post postNew = postRepository.getOne(postId);
          postNew.getPostVotes().add(p);
          postVoteRepository.save(p);
          response.setResult(true);
          return response;
        }
      } else {
        PostVote postVoteNew = PostVote.builder()
            .post(postRepository.getOne(postId))
            .user(userRepository.findUserByEmail(emailUser))
            .time(LocalDateTime.now())
            .value(dislike)
            .build();
        Post postNew = postRepository.getOne(postId);
        postNew.getPostVotes().add(postVoteNew);
        postVoteRepository.save(postVoteNew);
        response.setResult(true);
        return response;
      }
    }
    return response;
  }
}



