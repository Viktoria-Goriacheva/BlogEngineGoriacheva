package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.SettingsResponse;
import main.repository.GlobalSettingsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {

  private final GlobalSettingsRepository globalSettingsRepository;

  public SettingsResponse getGlobalSettings() {
    SettingsResponse settingsResponse = new SettingsResponse();
    settingsResponse.setMultiuserMode(true);
    settingsResponse.setPostPremoderation(true);
    return settingsResponse;
  }
}
