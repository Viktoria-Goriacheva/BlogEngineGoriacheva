package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.SettingsService;
import main.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGeneralController {

  private final SettingsService settingsService;
  private final InitResponse initResponse;
  private final TagService tagService;

  @GetMapping("/settings")
  public SettingsResponse settings() {
    return settingsService.getGlobalSettings();
  }

  @GetMapping("/init")
  public InitResponse init() {
    return initResponse;
  }

  @GetMapping("/tag")
  private ResponseEntity<TagResponse> getTag(
      @RequestParam(required = false, defaultValue = "") String query) {
    return ResponseEntity.ok(tagService.tag(query));
  }
}

