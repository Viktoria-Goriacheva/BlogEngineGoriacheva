package main.controller;

import main.api.response.AuthCheckResponse;
import main.service.AuthCheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

  AuthCheckService authCheckService;

  public ApiAuthController(AuthCheckService authCheckService) {
    this.authCheckService = authCheckService;
  }

  @GetMapping("/check")
  public ResponseEntity<AuthCheckResponse> authCheck() {
    return new ResponseEntity<>(authCheckService.getAuthCheck(), HttpStatus.OK);
  }
}

