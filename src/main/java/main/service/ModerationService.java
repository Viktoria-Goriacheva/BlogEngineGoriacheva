package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.ModerationResponse;
import main.model.ModerationStatus;
import main.model.Post;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModerationService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public ModerationResponse addStatusPost(Integer postId, String decision) {
    Post post = postRepository.getOne(postId);
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    Integer moderatorId = userRepository.findByIdUserForMod(email);
    post.setModeratorId(moderatorId);
    if (decision.equals("accept")) {
      post.setModerationStatus(ModerationStatus.ACCEPTED);
    } else if (decision.equals("decline")) {
      post.setModerationStatus(ModerationStatus.DECLINED);
    }
    postRepository.save(post);
    return new ModerationResponse(true);
  }
}
