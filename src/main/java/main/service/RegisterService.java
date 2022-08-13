package main.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import main.api.response.RegisterResponse;
import main.model.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Service
@RequiredArgsConstructor
public class RegisterService {

  private final CaptchaCodeRepository captchaCodeRepository;
  private final UserRepository userRepository;

  public RegisterResponse addNewUser(String email, String password, String name,
      String captcha, String secret) {
    RegisterResponse registerResponse = new RegisterResponse();
    Map<String, String> errors = checkInputData(email, captcha, secret);
    if (errors.isEmpty()) {
      registerResponse.setResult(true);
      registerResponse.setErrors(null);
      addUserInDB(email, password, name);
      return registerResponse;
    }
    registerResponse.setErrors(errors);
    registerResponse.setResult(false);
    return registerResponse;
  }

  private void addUserInDB(String email, String password, String name) {
    User user = User.builder().email(email).name(name).password(password).isModerator((byte) 0)
        .regTime(
            LocalDateTime.now()).build();
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
    if (userRepository.findByEmailUser(email.trim()).isPresent()) {
      result.put("email", "Такая почта уже зарегистрирована");
    }
  }

  public RegisterResponse getRegisterWithErrors(List<ObjectError> listErrors) {
    RegisterResponse response = new RegisterResponse();
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
