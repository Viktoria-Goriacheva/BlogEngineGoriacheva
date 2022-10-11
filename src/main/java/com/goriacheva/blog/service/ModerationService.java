package com.goriacheva.blog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.goriacheva.blog.api.response.ModerationResponse;
import com.goriacheva.blog.model.ModerationStatus;
import com.goriacheva.blog.model.Post;
import com.goriacheva.blog.repository.PostRepository;
import com.goriacheva.blog.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Log4j2
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
    log.info("Moderator {} set status {} at the post (id) = {}", email, decision, postId);
    return new ModerationResponse(true);
  }
}
