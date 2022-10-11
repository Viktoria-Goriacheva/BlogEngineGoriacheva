package com.goriacheva.blog.repository;

import java.util.Optional;
import com.goriacheva.blog.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaCodeRepository extends JpaRepository<CaptchaCode, Integer> {

  Optional<CaptchaCode> findBySecretCode(String code);

  boolean existsBySecretCode(String code);
}
