package main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Value("${upload.path}")
  private String uploadPath;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/upload/**")
        .addResourceLocations("file:upload/");
    registry.addResourceHandler("/js/**")
        .addResourceLocations("classpath:/static/js/");
    registry.addResourceHandler("/css/**")
        .addResourceLocations("classpath:/static/css/");
    registry.addResourceHandler("/img/**")
        .addResourceLocations("classpath:/static/img/");
    registry.addResourceHandler("/fonts/**")
        .addResourceLocations("classpath:/static/fonts/");
    registry.addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/");
  }

  @Bean(name = "multipartResolver")
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(1000000);
    return multipartResolver;
  }

}