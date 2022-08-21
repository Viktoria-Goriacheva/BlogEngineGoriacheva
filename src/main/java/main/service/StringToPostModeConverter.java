package main.service;

import java.util.Locale;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPostModeConverter implements Converter<String, PostMode> {

  @Override
  public PostMode convert(String source) {
    try {
      return source.isEmpty() ? null : PostMode.valueOf(source.trim().toUpperCase(Locale.ROOT));
    } catch (Exception e) {
      return PostMode.RECENT;
    }
  }
}
