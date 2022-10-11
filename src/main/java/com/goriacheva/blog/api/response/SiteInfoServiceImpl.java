package com.goriacheva.blog.api.response;

import com.goriacheva.blog.dto.SiteInfoDto;
import com.goriacheva.blog.service.SiteInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SiteInfoServiceImpl implements SiteInfoService {

  @Value("${blog.title}")
  private String title;
  @Value("${blog.subtitle}")
  private String subtitle;
  @Value("${blog.phone}")
  private String phone;
  @Value("${blog.email}")
  private String email;
  @Value("${blog.copyright}")
  private String copyright;
  @Value("${blog.copyrightFrom}")
  private String copyrightFrom;

  public SiteInfoDto getSiteInfo() {

    return new SiteInfoDto(title, subtitle, phone, email, copyright, copyrightFrom);

  }
}

