package main.repository;

import java.util.Optional;
import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptchaCodeRepository extends JpaRepository<CaptchaCode, Integer> {

  Optional<CaptchaCode> findBySecretCode(String code);

  boolean existsBySecretCode(String code);
}
