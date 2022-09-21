package main.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import main.api.response.StatusResponse;
import main.model.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RegisterService {

  private final CaptchaCodeRepository captchaCodeRepository;
  private final UserRepository userRepository;
  private final ImageService imageService;
  private Integer maxSizePhoto = 5242880;

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

  public StatusResponse changeMyProfileWithoutPhoto(String name, String email, String password,
      int removePhoto) {
    StatusResponse response = new StatusResponse();
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    Optional<User> userOptional = userRepository.findByEmail(userEmail);
    User user = userOptional.get();

    Map<String, String> errors = new HashMap<>();
    if (userOptional.isPresent() && !(user.getEmail().equals(userEmail))) {
      errors.put("email", "Этот e-mail уже зарегистрирован");
      response.setErrors(errors);
      return response;
    }
    user.setName(name);
    user.setEmail(email);
    if (!(password == null)) {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
      user.setPassword(encoder.encode(password));
    }
    if (removePhoto == 1) {
      user.setPhoto(null);
    }
    userRepository.save(user);
    response.setResult(true);
    return response;
  }

  public StatusResponse changeMyProfileWithPhoto(MultipartFile photo, String name, String email,
      String password) {
    StatusResponse response = new StatusResponse();
    Map<String, String> errors = new HashMap<>();
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    Optional<User> userOptional = userRepository.findByEmail(userEmail);
    User user = userOptional.get();
    if (userOptional.isPresent() && !(user.getEmail().equals(userEmail))) {
      errors.put("email", "Этот e-mail уже зарегистрирован");
      response.setErrors(errors);
      return response;
    }
    user.setName(name);
    user.setEmail(email);
    String imageType = photo.getContentType().split("/")[1];
    if (!photo.isEmpty()) {
      if ((imageType.equals("jpg") || imageType.equals("jpeg")
          || imageType.equals("png"))) {
        if (photo.getSize() > maxSizePhoto) {
          errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
        } else {
          try {
            String photoLine = imageService.uploadFileAndGetPath(photo, true);
            user.setPhoto(photoLine);
          } catch (IOException e) {
            e.printStackTrace();
          }
          if (!(password == null)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(password));
          }
          userRepository.save(user);
          response.setResult(true);
          return response;
        }
      } else {
        errors.put("photo", "Некорректный формат файла. Допустимые форматы: png, jpg(jpeg)");
        response.setErrors(errors);
      }
      response.setResult(false);
    }
    return response;
  }
}
