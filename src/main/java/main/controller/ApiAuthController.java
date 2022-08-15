package main.controller;

import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.RegisterResponse;
import main.service.CaptchaService;
import main.service.LoginService;
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

  private final CaptchaService captchaService;
  private final RegisterService registerService;
  private final LoginService loginService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> getRegister(@RequestBody LoginRequest user) {
    return ResponseEntity.ok(loginService.getLogin(user.getEmail(), user.getPassword()));
  }

  @GetMapping("/check")
  public ResponseEntity<LoginResponse> check(Principal principal) {
    return ResponseEntity.ok(loginService.checkUser(principal));
  }
  @GetMapping("/logout")
  public ResponseEntity<LoginResponse> logout() {
    return ResponseEntity.ok(loginService.logout());
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