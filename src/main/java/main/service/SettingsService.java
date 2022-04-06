package main.service;

import main.api.response.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
public SettingsResponse getGlobalSettings(){
  SettingsResponse settingsResponse = new SettingsResponse();
  settingsResponse.setMultiuserMode(true);
  settingsResponse.setPostPremoderation(true);
  return settingsResponse;
}
}
