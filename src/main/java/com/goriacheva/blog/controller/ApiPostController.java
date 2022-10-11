package com.goriacheva.blog.controller;

import com.goriacheva.blog.api.response.PostIdResponse;
import com.goriacheva.blog.api.response.PostResponse;
import com.goriacheva.blog.model.ModerationStatus;
import com.goriacheva.blog.model.PostStatus;
import com.goriacheva.blog.repository.PostRepository;
import com.goriacheva.blog.service.PostMode;
import com.goriacheva.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import com.goriacheva.blog.api.request.AddPostRequest;
import com.goriacheva.blog.api.request.VoteRequest;
import com.goriacheva.blog.api.response.ModerationResponse;
import com.goriacheva.blog.api.response.StatusResponse;
import com.goriacheva.blog.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {

  private final PostService postService;
  private final PostRepository postRepository;
  private final VoteService voteService;

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

  @GetMapping("/search")
  public ResponseEntity<PostResponse> getPostQuery(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit,
      @RequestParam(value = "query") String query) {

    return ResponseEntity.ok(postService.getSearchPostQuery(offset, limit, query));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostIdResponse> getPostWithId(@PathVariable Integer id) {
    return (postRepository.existsById(id)) ?
        ResponseEntity.ok(postService.getPostId(id)) :
        ResponseEntity.notFound().build();
  }

  @GetMapping("/my")
  public ResponseEntity<PostResponse> getMyPost(
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit,
      @RequestParam(value = "status") PostStatus status) {

    return ResponseEntity.ok(postService.getMyPosts(offset, limit, status));
  }

  @GetMapping("/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  public ResponseEntity<PostResponse> getPostForModeration(
      @RequestParam(value = "status") ModerationStatus status,
      @RequestParam(required = false, defaultValue = "0") int offset,
      @RequestParam(required = false, defaultValue = "10") int limit) {

    return ResponseEntity.ok(postService.getAllPostsForModeratoin(status, offset, limit));
  }

  @PostMapping
  public ResponseEntity<StatusResponse> addPost(@RequestBody AddPostRequest addPost) {
    return ResponseEntity.ok(postService.addPost(addPost.getTime(), addPost.getActive(),
        addPost.getTitle(), addPost.getTags(), addPost.getText()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<StatusResponse> changePost(@RequestBody AddPostRequest addPost,
      @PathVariable Integer id) {
    return ResponseEntity.ok(postService.changePost(addPost.getTime(), addPost.getActive(),
        addPost.getTitle(), addPost.getTags(), addPost.getText(), id));
  }

  @PostMapping("/like")
  public ResponseEntity<ModerationResponse> setLike(@RequestBody VoteRequest voteRequest) {
    return ResponseEntity.ok(voteService.setLike(voteRequest.getPostId()));
  }

  @PostMapping("/dislike")
  public ResponseEntity<ModerationResponse> setDislike(@RequestBody VoteRequest voteRequest) {
    return ResponseEntity.ok(voteService.setDislike(voteRequest.getPostId()));
  }
}

