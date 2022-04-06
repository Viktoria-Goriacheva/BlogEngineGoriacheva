package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.service.SettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {
private SettingsService settingsService;
  private InitResponse initResponse;

  public ApiGeneralController(SettingsService settingsService, InitResponse initResponse) {
    this.settingsService = settingsService;
    this.initResponse = initResponse;
  }
  @GetMapping("/settings")
  public SettingsResponse settings() {
    return settingsService.getGlobalSettings();
  }

  @GetMapping("/init")
  public InitResponse init() {
    return initResponse;
  }
}

