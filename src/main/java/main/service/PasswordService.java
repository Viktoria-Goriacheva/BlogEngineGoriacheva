package main.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.api.response.ModerationResponse;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

  private final UserRepository userRepository;
  private final JavaMailSender javaMailSender;
  private final ServletWebServerApplicationContext webServerAppContext;

  public ModerationResponse changePassword(String email, HttpServletRequest request)
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
}
