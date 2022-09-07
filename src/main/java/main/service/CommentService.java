package main.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import main.api.response.CommentResponse;
import main.model.Post;
import main.model.PostComment;
import main.model.User;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostCommentRepository postCommentRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  public CommentResponse addComment(Integer parentId, Integer postId, String text) {
    CommentResponse response = new CommentResponse();
    Map<String, String> errors = checkText(text);
    if (!errors.isEmpty()) {
      response.setErrors(errors);
      response.setResult(false);
      return response;
    }
    Post post = postRepository.getOne(postId);
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findUserByEmail(userEmail);
    PostComment postComment = PostComment.builder()
        .post(post)
        .user(user)
        .regTime(LocalDateTime.now())
        .text(text)
        .parent(null)
        .build();
    if (!(parentId == null)) {
      postComment.setParent(postCommentRepository.getOne(parentId));
    }
    postCommentRepository.save(postComment);
    response.setResult(true);
    response.setErrors(null);
    return response;
  }

  private Map<String, String> checkText(String text) {
    Map<String, String> result = new HashMap<>();
    if (text.isEmpty()) {
      result.put("text", "Текст комментария не задан");
    }
    if (text.length() < 3) {
      result.put("text", "Текст комментария слишком короткий");
    }
    return result;
  }
}
