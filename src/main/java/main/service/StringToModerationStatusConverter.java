package main.service;

import java.util.Locale;
import main.model.ModerationStatus;
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
