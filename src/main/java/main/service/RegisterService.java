package main.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import main.api.response.StatusResponse;
import main.model.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Service
@RequiredArgsConstructor
public class RegisterService {

  private final CaptchaCodeRepository captchaCodeRepository;
  private final UserRepository userRepository;

  public StatusResponse addNewUser(String email, String password, String name,
      String captcha, String secret) {
    StatusResponse statusResponse = new StatusResponse();
    Map<String, String> errors = checkInputData(email, captcha, secret);
    if (errors.isEmpty()) {
      statusResponse.setResult(true);
      statusResponse.setErrors(null);
      addUserInDB(email, password, name);
      return statusResponse;
    }
    statusResponse.setErrors(errors);
    statusResponse.setResult(false);
    return statusResponse;
  }

  private void addUserInDB(String email, String password, String name) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    User user = User.builder()
        .email(email)
        .name(name)
        .password(passwordEncoder.encode(password))
        .isModerator((byte) 0)
        .regTime(LocalDateTime.now())
        .build();
    userRepository.save(user);
  }

  private Map<String, String> checkInputData(String email, String captcha, String secret) {
    Map<String, String> result = new TreeMap<>();
    checkEmail(email, result);
    checkCaptcha(captcha, secret, result);
    return result;
  }

  private void checkCaptcha(String captcha, String secret, Map<String, String> result) {
    if (captchaCodeRepository.existsBySecretCode(secret)) {
      String code = captchaCodeRepository.findBySecretCode(secret).get().getCode();
      if (!code.equals(captcha)) {
        result.put("captcha", "Код с картинки введён неверно");
      }
      return;
    }
    result.put("captcha", "Код с картинки введён неверно");
  }

  private void checkEmail(String email, Map<String, String> result) {
    if (userRepository.findByEmail(email.trim()).isPresent()) {
      result.put("email", "Такая почта уже зарегистрирована");
    }
  }

  public StatusResponse getRegisterWithErrors(List<ObjectError> listErrors) {
    StatusResponse response = new StatusResponse();
    Map<String, String> errors = new HashMap<>();
    listErrors.forEach((error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    }));
    response.setResult(false);
    response.setErrors(errors);
    return response;
  }
}
