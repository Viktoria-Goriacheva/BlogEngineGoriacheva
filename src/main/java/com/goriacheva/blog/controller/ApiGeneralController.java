package com.goriacheva.blog.controller;

import com.goriacheva.blog.api.response.SettingsResponse;
import com.goriacheva.blog.dto.SiteInfoDto;
import com.goriacheva.blog.model.User;
import com.goriacheva.blog.service.ImageService;
import com.goriacheva.blog.service.ModerationService;
import com.goriacheva.blog.service.StatisticService;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.goriacheva.blog.api.request.CommentRequest;
import com.goriacheva.blog.api.request.ModerationRequest;
import com.goriacheva.blog.api.request.ProfileRequest;
import com.goriacheva.blog.api.response.CalendarResponse;
import com.goriacheva.blog.api.response.CommentResponse;
import com.goriacheva.blog.api.response.ModerationResponse;
import com.goriacheva.blog.api.response.StatisticsResponse;
import com.goriacheva.blog.api.response.StatusResponse;
import com.goriacheva.blog.api.response.TagResponse;
import com.goriacheva.blog.model.GlobalSettings;
import com.goriacheva.blog.repository.GlobalSettingsRepository;
import com.goriacheva.blog.repository.PostCommentRepository;
import com.goriacheva.blog.repository.UserRepository;
import com.goriacheva.blog.service.CalendarService;
import com.goriacheva.blog.service.CommentService;
import com.goriacheva.blog.service.RegisterService;
import com.goriacheva.blog.service.SettingsService;
import com.goriacheva.blog.service.SiteInfoService;
import com.goriacheva.blog.service.TagService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

  @Value("${upload.path}")
  private String uploadPath;
  private final SettingsService settingsService;
  private final SiteInfoService siteInfoService;
  private final TagService tagService;
  private final CalendarService calendarService;
  private final ModerationService moderationService;
  private final ImageService imageService;
  private final CommentService commentService;
  private final PostCommentRepository postCommentRepository;
  private final StatisticService statisticService;
  private final GlobalSettingsRepository globalSettingsRepository;
  private final UserRepository userRepository;
  private final RegisterService registerService;

  @GetMapping("/settings")
  private ResponseEntity<SettingsResponse> settings() {
    return ResponseEntity.ok(settingsService.getGlobalSettings());
  }

  @GetMapping("/init")
  private SiteInfoDto init() {
    return siteInfoService.getSiteInfo();
  }

  @GetMapping("/tag")
  private ResponseEntity<TagResponse> tag(
      @RequestParam(required = false, defaultValue = "") String query) {
    return ResponseEntity.ok(tagService.tag(query));
  }

  @GetMapping("/calendar")
  private ResponseEntity<CalendarResponse> calendar(
      @RequestParam(required = false, defaultValue = "") String year) {
    return (year.isEmpty()) ? ResponseEntity.ok(
        calendarService.calendar(String.valueOf(LocalDateTime.now().getYear())))
        : ResponseEntity.ok(calendarService.calendar(year));
  }

  @PostMapping("/moderation")
  public ResponseEntity<ModerationResponse> changeStatus(@RequestBody ModerationRequest request,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.ok(new ModerationResponse(false));
    }
    return ResponseEntity.ok(
        moderationService.addStatusPost(request.getPostId(), request.getDecision()));
  }

  @PostMapping(value = "/image", consumes = {"multipart/form-data"})
  public ResponseEntity<?> uploadImage(@RequestParam MultipartFile image) throws IOException {
    StatusResponse response = imageService.postImage(image);
    if (response.isResult()) {
      return ResponseEntity.ok(response.getPath());
    }
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/comment")
  public ResponseEntity<?> addPost(@RequestBody CommentRequest commentRequest) {
    CommentResponse commentResponse = commentService.addComment(commentRequest.getParentId(),
        commentRequest.getPostId(), commentRequest.getText());
    if (commentResponse.isResult()) {
      return ResponseEntity.ok(postCommentRepository.findByIdOrderByIdDesc().getId());
    }
    return new ResponseEntity<>(commentResponse, HttpStatus.BAD_REQUEST);
  }

  @PutMapping("/settings")
  public ResponseEntity<?> saveSettings(@RequestBody SettingsResponse settingsResponse) {
    settingsService.saveSettings(settingsResponse);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping("/statistics/my")
  private ResponseEntity<StatisticsResponse> statistic() {
    return ResponseEntity.ok(statisticService.getStatistic());
  }

  @GetMapping("/statistics/all")
  private ResponseEntity<StatisticsResponse> statisticAll() {
    GlobalSettings mode = globalSettingsRepository.findByCode("STATISTICS_IS_PUBLIC");
    if (mode.getValue().equals("NO") && (SecurityContextHolder.getContext().getAuthentication()
        .isAuthenticated())) {
      String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
      User user = userRepository.findUserByEmail(userEmail);
      if (user.getIsModerator() == 0) {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
      }
    }
    return ResponseEntity.ok(statisticService.getStatisticAll());
  }

  @PostMapping(value = "/profile/my", consumes = {"application/json"})
  public ResponseEntity<StatusResponse> changeMyProfileWithoutPhoto(
      @RequestBody @Valid ProfileRequest editProfileRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest()
          .body(registerService.getRegisterWithErrors(bindingResult.getAllErrors()));
    }
    return ResponseEntity.ok(registerService.changeMyProfileWithoutPhoto(
        editProfileRequest.getName(),
        editProfileRequest.getEmail(),
        editProfileRequest.getPassword(),
        editProfileRequest.getRemovePhoto()));
  }

  @PostMapping(value = "/profile/my", consumes = {"multipart/form-data"})
  public ResponseEntity<StatusResponse> changeMyProfileWithPhoto(
      @RequestParam MultipartFile photo,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String password) {
    StatusResponse statusResponse = registerService.changeMyProfileWithPhoto(photo, name, email,
        password);
    if (statusResponse.isResult()) {
      return ResponseEntity.ok(statusResponse);
    }
    return ResponseEntity.badRequest().body(statusResponse);
  }
}

