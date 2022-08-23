package main.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import main.api.request.ModerationRequest;
import main.api.response.CalendarResponse;
import main.api.response.ModerationResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.dto.SiteInfoDTO;
import main.service.CalendarService;
import main.service.ModerationService;
import main.service.SettingsService;
import main.service.SiteInfoService;
import main.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

  private final SettingsService settingsService;
  private final SiteInfoService siteInfoService;
  private final TagService tagService;
  private final CalendarService calendarService;
  private final ModerationService moderationService;

  @GetMapping("/settings")
  private SettingsResponse settings() {
    return settingsService.getGlobalSettings();
  }

  @GetMapping("/init")
  private SiteInfoDTO init() {
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
}

