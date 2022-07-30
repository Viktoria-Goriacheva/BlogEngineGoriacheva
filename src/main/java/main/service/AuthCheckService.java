package main.service;

import main.api.response.AuthCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthCheckService {

  public AuthCheckResponse getAuthCheck() {
    AuthCheckResponse authCheckTrueResponse = new AuthCheckResponse(false);
    return authCheckTrueResponse;
  }
}
