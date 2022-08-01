package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.PostResponse;
import main.service.PostMode;
import main.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiPostController {

  private final PostService postService;

  @GetMapping("/post")
  public ResponseEntity<PostResponse> getPost(
      @RequestParam(value = "recent", required = false, defaultValue = "RECENT") PostMode mode,
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit) {

    return ResponseEntity.ok(postService.getAllPosts(mode, offset, limit));
  }
}

