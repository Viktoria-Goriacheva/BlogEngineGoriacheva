package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.PostIdResponse;
import main.api.response.PostResponse;
import main.repository.PostRepository;
import main.service.PostMode;
import main.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {

  private final PostService postService;
  private final PostRepository postRepository;

  @GetMapping
  public ResponseEntity<PostResponse> getPost(
      @RequestParam(required = false, defaultValue = "RECENT") PostMode mode,
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit) {

    return ResponseEntity.ok(postService.getAllPosts(mode, offset, limit));
  }

  @GetMapping("/byDate")
  public ResponseEntity<PostResponse> getByDate(
      @RequestParam(value = "date") String date,
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit) {

    return ResponseEntity.ok(postService.getByDate(date, offset, limit));
  }

  @GetMapping("/byTag")
  public ResponseEntity<PostResponse> getByTag(
      @RequestParam(value = "tag") String tag,
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit) {

    return ResponseEntity.ok(postService.getByTag(tag, offset, limit));
  }

  @GetMapping("/{ID}")
  public ResponseEntity<PostIdResponse> getPostWithId(@PathVariable Integer ID) {
    return (postRepository.existsById(ID)) ?
        ResponseEntity.ok(postService.getPostId(ID)) :
        ResponseEntity.notFound().build();
  }
}

