package com.goriacheva.blog.config;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import com.goriacheva.blog.model.GlobalSettings;
import com.goriacheva.blog.repository.GlobalSettingsRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InitSetting {

  private final GlobalSettingsRepository globalSettingsRepository;

  @PostConstruct
  private void postConstruct() {
    globalSettingsRepository.deleteAll();
    List<GlobalSettings> globalSettingsList = new ArrayList<>();
    GlobalSettings multiuserMode = new GlobalSettings("MULTIUSER_MODE",
        "Многопользовательский режим", "YES");
    GlobalSettings postPremoderation = new GlobalSettings("POST_PREMODERATION",
        "Премодерация постов ", "YES");
    GlobalSettings statisticsIsPublic = new GlobalSettings("STATISTICS_IS_PUBLIC",
        "Показывать всем статистику блога", "YES");
    globalSettingsList.add(multiuserMode);
    globalSettingsList.add(postPremoderation);
    globalSettingsList.add(statisticsIsPublic);
    globalSettingsRepository.saveAll(globalSettingsList);
  }
}