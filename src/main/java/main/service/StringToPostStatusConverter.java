package main.service;

import java.util.Locale;
import main.model.PostStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPostStatusConverter implements Converter<String, PostStatus> {

  @Override
  public PostStatus convert(String source) {
    try {
      return source.isEmpty() ? null : PostStatus.valueOf(source.trim().toUpperCase(Locale.ROOT));
    } catch (Exception e) {
      return null;
    }
  }
}