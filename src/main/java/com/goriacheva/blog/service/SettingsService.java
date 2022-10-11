package com.goriacheva.blog.service;

import com.goriacheva.blog.api.response.SettingsResponse;
import lombok.RequiredArgsConstructor;
import com.goriacheva.blog.model.GlobalSettings;
import com.goriacheva.blog.repository.GlobalSettingsRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {

  private final static String YES = "YES";
  private final static String NO = "NO";
  private final GlobalSettingsRepository globalSettingsRepository;

  public SettingsResponse getGlobalSettings() {
    SettingsResponse settingsResponse = new SettingsResponse();
    GlobalSettings multiUserMode = globalSettingsRepository.findByCode("MULTIUSER_MODE");
    GlobalSettings postPremoderation = globalSettingsRepository.findByCode("POST_PREMODERATION");
    GlobalSettings statisticsIsPublic = globalSettingsRepository.findByCode("STATISTICS_IS_PUBLIC");

    switch (multiUserMode.getValue()) {
      case (YES):
        settingsResponse.setMultiuserMode(true);
        break;
      case (NO):
        settingsResponse.setMultiuserMode(false);
        break;
    }
    switch (postPremoderation.getValue()) {
      case (YES):
        settingsResponse.setPostPremoderation(true);
        break;
      case (NO):
        settingsResponse.setPostPremoderation(false);
        break;
    }
    switch (statisticsIsPublic.getValue()) {
      case (YES):
        settingsResponse.setStatisticsIsPublic(true);
        break;
      case (NO):
        settingsResponse.setStatisticsIsPublic(false);
        break;
    }
    return settingsResponse;
  }
  @PreAuthorize("hasAuthority('user:moderate')")
  public void saveSettings(SettingsResponse settings) {
    GlobalSettings multiUserMode = globalSettingsRepository.findByCode("MULTIUSER_MODE");
    GlobalSettings postPremoderation = globalSettingsRepository.findByCode("POST_PREMODERATION");
    GlobalSettings statisticsIsPublic = globalSettingsRepository.findByCode("STATISTICS_IS_PUBLIC");

    if (settings.isMultiuserMode()) {
      multiUserMode.setValue(YES);
    } else {
      multiUserMode.setValue(NO);
    }

    if (settings.isPostPremoderation()) {
      postPremoderation.setValue(YES);
    } else {
      postPremoderation.setValue(NO);
    }

    if (settings.isStatisticsIsPublic()) {
      statisticsIsPublic.setValue(YES);
    } else {
      statisticsIsPublic.setValue(NO);
    }

    globalSettingsRepository.save(multiUserMode);
    globalSettingsRepository.save(postPremoderation);
    globalSettingsRepository.save(statisticsIsPublic);
  }
}
