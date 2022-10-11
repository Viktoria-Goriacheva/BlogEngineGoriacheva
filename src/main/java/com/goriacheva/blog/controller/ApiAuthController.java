package com.goriacheva.blog.controller;

import com.goriacheva.blog.api.request.LoginRequest;
import com.goriacheva.blog.api.request.RegisterRequest;
import com.goriacheva.blog.api.response.CaptchaResponse;
import com.goriacheva.blog.api.response.LoginResponse;
import com.goriacheva.blog.repository.GlobalSettingsRepository;
import com.goriacheva.blog.service.CaptchaService;
import com.goriacheva.blog.service.LoginService;
import com.goriacheva.blog.service.RegisterService;
import java.net.MalformedURLException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.goriacheva.blog.api.request.ChangePasswordRequest;
import com.goriacheva.blog.api.request.RestoreRequest;
import com.goriacheva.blog.api.response.ModerationResponse;
import com.goriacheva.blog.api.response.StatusResponse;
import com.goriacheva.blog.model.GlobalSettings;
import com.goriacheva.blog.service.PasswordService;
import org.springframework.http.HttpStatus;
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
  private final PasswordService passwordService;
  private final GlobalSettingsRepository globalSettingsRepository;

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
  public ResponseEntity<StatusResponse> getRegister(@Valid @RequestBody RegisterRequest user,
      BindingResult bindingResult) {
    GlobalSettings mode = globalSettingsRepository.findByCode("MULTIUSER_MODE");
    if (mode.getValue().equals("NO")) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    if (bindingResult.hasErrors()) {
      return ResponseEntity.ok(registerService.getRegisterWithErrors(bindingResult.getAllErrors()));
    }
    return ResponseEntity.ok(registerService.addNewUser(user.getEMail(), user.getPassword(),
        user.getName(), user.getCaptcha(), user.getCaptchaSecret()));
  }

  @PostMapping("/restore")
  public ResponseEntity<ModerationResponse> restorePassword(
      @RequestBody RestoreRequest restoreRequest, HttpServletRequest request)
      throws MalformedURLException {
    return ResponseEntity.ok(passwordService.restorePassword(restoreRequest.getEmail(), request));
  }

  @PostMapping("/password")
  public ResponseEntity<StatusResponse> changePassword(@Valid
  @RequestBody ChangePasswordRequest changeRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.ok(registerService.getRegisterWithErrors(bindingResult.getAllErrors()));
    }
    return ResponseEntity.ok(
        passwordService.changePassword(changeRequest.getCode(), changeRequest.getPassword(),
            changeRequest.getCaptcha(), changeRequest.getCaptchaSecret()));
  }
}