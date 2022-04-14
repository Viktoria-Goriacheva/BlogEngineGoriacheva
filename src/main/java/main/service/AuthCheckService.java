package main.service;

import main.api.response.AuthCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthCheckService {
  public AuthCheckResponse getAuthCheck() {
    AuthCheckResponse authCheckTrueResponse = new AuthCheckResponse(false);
//    authCheckTrueResponse.getUser().setId(1);
//    authCheckTrueResponse.getUser().setModerationCount(76);
//    authCheckTrueResponse.getUser().setSettings(true);
    return authCheckTrueResponse;
  }
}
