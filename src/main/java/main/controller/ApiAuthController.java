package main.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.api.response.AuthCheckResponse;
import main.api.response.CaptchaResponse;
import main.api.response.RegisterResponse;
import main.api.request.RegisterRequest;
import main.service.AuthCheckService;
import main.service.CaptchaService;
import main.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiAuthController {

  private final AuthCheckService authCheckService;
  private final CaptchaService captchaService;
  private final RegisterService registerService;

  @GetMapping("/check")
  public ResponseEntity<AuthCheckResponse> authCheck() {
    return ResponseEntity.ok(authCheckService.getAuthCheck());
  }

  @GetMapping("/captcha")
  public ResponseEntity<CaptchaResponse> getCaptcha() {
    return ResponseEntity.ok(captchaService.getCaptchaCode());
  }
  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> getRegister(@Valid @RequestBody RegisterRequest user,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.ok(registerService.getRegisterWithErrors(bindingResult.getAllErrors()));
    }
    return ResponseEntity.ok(registerService.addNewUser(user.getEMail(), user.getPassword(),
        user.getName(), user.getCaptcha(), user.getCaptchaSecret()));
  }
}