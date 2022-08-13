package main.service;

import com.github.cage.GCage;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.repository.CaptchaCodeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptchaService {

  private final CaptchaCodeRepository captchaCodeRepository;

  public CaptchaResponse getCaptchaCode() {
    deleteOldCaptchaCodes();
    CaptchaResponse captchaResponse = new CaptchaResponse();
    GCage captcha = new GCage();
    String codeCaptcha = captcha.getTokenGenerator().next();
    String image = getImageBase64(captcha, codeCaptcha);
    String secretCode = generateSecretCode();
    captchaResponse.setImageBase64(image);
    captchaResponse.setSecretCode(secretCode);
    saveCaptchaToDb(codeCaptcha, secretCode);
    return captchaResponse;
  }

  private String getImageBase64(GCage gCage, String code) {
    byte[] fileContent = gCage.draw(code);
    String encodedString = Base64.getEncoder().encodeToString(fileContent);
    return "data:image/png;base64, " + encodedString;
  }

  private String generateSecretCode() {
    return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
  }


  private void saveCaptchaToDb(String codeCaptcha, String secretCode) {
    CaptchaCode captcha = new CaptchaCode();
    captcha.setSecretCode(secretCode);
    captcha.setCode(codeCaptcha);
    captcha.setTime(LocalDateTime.now());
    captchaCodeRepository.save(captcha);
  }

  private void deleteOldCaptchaCodes() {
    List<CaptchaCode> allCaptcha = captchaCodeRepository.findAll();
    LocalDateTime boundaryTime = LocalDateTime.now().minusHours(1);
    for (CaptchaCode captcha : allCaptcha) {
      if (captcha.getTime().isBefore(boundaryTime)) {
        captchaCodeRepository.delete(captcha);
      }
    }
  }
}
