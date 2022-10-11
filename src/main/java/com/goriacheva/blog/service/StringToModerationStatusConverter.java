package com.goriacheva.blog.service;

import com.goriacheva.blog.model.ModerationStatus;
import java.util.Locale;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToModerationStatusConverter implements Converter<String, ModerationStatus> {

  @Override
  public ModerationStatus convert(String source) {
    try {
      return source.isEmpty() ? null : ModerationStatus.valueOf(source.trim().toUpperCase(Locale.ROOT));
    } catch (Exception e) {
      return null;
    }
  }
}
