package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.response.AuthCheckResponse;
import main.api.response.CaptchaResponse;
import main.service.AuthCheckService;
import main.service.CaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiAuthController {

  private final AuthCheckService authCheckService;
  private final CaptchaService captchaService;

  @GetMapping("/check")
  public ResponseEntity<AuthCheckResponse> authCheck() {
    return ResponseEntity.ok(authCheckService.getAuthCheck());
  }

  @GetMapping("/captcha")
  public ResponseEntity<CaptchaResponse> getCaptcha() {
    return ResponseEntity.ok(captchaService.getCaptchaCode());
  }
}

