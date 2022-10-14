package com.goriacheva.blog.service;

import com.goriacheva.blog.api.response.ModerationResponse;
import com.goriacheva.blog.api.response.StatusResponse;
import com.goriacheva.blog.model.User;
import com.goriacheva.blog.repository.CaptchaCodeRepository;
import com.goriacheva.blog.repository.UserRepository;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.goriacheva.blog.model.CaptchaCode;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class PasswordService {

  private final UserRepository userRepository;
  private final CaptchaCodeRepository captchaCodeRepository;
  private final JavaMailSender javaMailSender;
  private final ServletWebServerApplicationContext webServerAppContext;

  public ModerationResponse restorePassword(String email, HttpServletRequest request) {
    ModerationResponse response = new ModerationResponse();
    Optional<User> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return response;
    }
    String hash = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    user.get().setCode(hash);
    URL url = null;
    try {
      url = new URL(request.getRequestURL().toString());
    } catch (MalformedURLException e) {
      log.error(e.getMessage());
    }
    int port = webServerAppContext.getWebServer().getPort();
    StringBuilder message = new StringBuilder("Перейдите по ссылке для восстановления пароля: "
        + url.getProtocol() + "://" + url.getHost() + ":" + port + "/login/change-password/");
    message.append(hash);
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom("grumbla@gmail.co");
    simpleMailMessage.setTo(email);
    simpleMailMessage.setSubject("Восстановление пароля");
    simpleMailMessage.setText(message.toString());
    javaMailSender.send(simpleMailMessage);
    log.info("{} requested to restore password", email);
    userRepository.save(user.get());
    response.setResult(true);
    return response;
  }

  public StatusResponse changePassword(String code, String password, String captcha,
      String captchaSecret) {
    StatusResponse statusResponse = new StatusResponse();
    Map<String, String> errors = checkErrors(code, captchaSecret, captcha);
    if (!errors.isEmpty()) {
      statusResponse.setErrors(errors);
      return statusResponse;
    }
    saveNewPassword(code, password);
    statusResponse.setResult(true);
    return statusResponse;
  }

  private Map<String, String> checkErrors(String code, String captchaSecret, String captcha) {
    HashMap<String, String> errors = new HashMap<>();
    Optional<User> user = userRepository.findByCode(code);
    if (user.isEmpty()) {
      errors.put("code", "Ссылка для восстановления пароля устарела." +
          "<a href = \"/auth/restore\">Запросить ссылку снова</a>");
      return errors;
    }

    CaptchaCode captchaCode = captchaCodeRepository.findBySecretCode(captchaSecret).orElseThrow();
    if (!captchaCode.getCode().equals(captcha)) {
      errors.put("captcha", "Код с картинки введен неверно.");
      return errors;
    }
    return errors;
  }

  private void saveNewPassword(String code, String password) {
    User user = userRepository.findByCode(code).orElseThrow();
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    user.setPassword(passwordEncoder.encode(password));
    log.info("Password {} has been successfully saved", user.getEmail());
    userRepository.save(user);
  }
}
