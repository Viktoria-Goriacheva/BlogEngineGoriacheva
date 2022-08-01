package main.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SiteInfoDTO {

  private final String title;
  private final String subtitle;
  private final String phone;
  private final String email;
  private final String copyright;
  private final String copyrightFrom;

}
