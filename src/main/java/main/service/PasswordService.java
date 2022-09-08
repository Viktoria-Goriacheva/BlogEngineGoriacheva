package main.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.api.response.ModerationResponse;
import main.api.response.StatusResponse;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaCodeRepository;
import main.repository.UserRepository;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

  private final UserRepository userRepository;
  private final CaptchaCodeRepository captchaCodeRepository;
  private final JavaMailSender javaMailSender;
  private final ServletWebServerApplicationContext webServerAppContext;

  public ModerationResponse restorePassword(String email, HttpServletRequest request)
      throws MalformedURLException {
    ModerationResponse response = new ModerationResponse();
    Optional<User> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return response;
    }
    String hash = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    user.get().setCode(hash);
    URL url = new URL(request.getRequestURL().toString());
    int port = webServerAppContext.getWebServer().getPort();
    StringBuilder message = new StringBuilder("Перейдите по ссылке для восстановления пароля: "
        + url.getProtocol() + "://" + url.getHost() + ":" + port + "/login/change-password/");
    message.append(hash);
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom("grumblaa@gmail.com");
    simpleMailMessage.setTo(email);
    simpleMailMessage.setSubject("Восстановление пароля");
    simpleMailMessage.setText(message.toString());
    javaMailSender.send(simpleMailMessage);
    userRepository.save(user.get());
    response.setResult(true);
    return response;
  }
  public StatusResponse changePassword(String code,String password,String captcha,String captchaSecret){
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
    userRepository.save(user);
  }
}
