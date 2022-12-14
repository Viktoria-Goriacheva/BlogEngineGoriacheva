package com.goriacheva.blog.service;

import com.goriacheva.blog.api.response.LoginResponse;
import com.goriacheva.blog.model.User;
import java.security.Principal;
import java.util.Optional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.goriacheva.blog.dto.UserDto;
import com.goriacheva.blog.repository.PostRepository;
import com.goriacheva.blog.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Data
@Log4j2
@Service
@RequiredArgsConstructor
public class LoginService {

  private final UserRepository userRepository;

  private final PostRepository postRepository;

  private final UserDetailsService userDetailsService;

  private final AuthenticationManager authenticationManager;


  public LoginResponse checkUser(Principal principal) {
    LoginResponse loginResponse = new LoginResponse();
    if (principal == null) {
      return loginResponse;
    }
    UserDto user = prepareUser(principal.getName());
    loginResponse.setResult(true);
    loginResponse.setUser(user);
    return loginResponse;
  }

  public LoginResponse logout() {
    SecurityContextHolder.clearContext();
    LoginResponse response = new LoginResponse();
    response.setResult(true);
    return response;
  }

  public LoginResponse getLogin(String email, String password) {
    LoginResponse loginResponse = new LoginResponse();
    if (!findUser(email, password)) {
      loginResponse.setResult(false);
      return loginResponse;
    }
    Authentication auth = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password));
    SecurityContextHolder.getContext().setAuthentication(auth);
    UserDto userDto = prepareUser(email);
    loginResponse.setResult(true);
    loginResponse.setUser(userDto);
    log.info("{} logged to blog", email);
    return loginResponse;
  }

  private UserDto prepareUser(String email) {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    UserDto userDto = UserDto.builder()
        .id(user.getId())
        .name(user.getName())
        .photo(user.getPhoto())
        .email(user.getEmail())
        .build();
    setSettingsAndModerationStatus(user, userDto);
    setModerationCount(userDto);

    return userDto;
  }

  private boolean findUser(String email, String password) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    Optional<User> user = userRepository.findByEmail(email);
    return user.filter(value -> passwordEncoder.matches(password, value.getPassword())).isPresent();
  }

  private void setModerationCount(UserDto userDto) {
    if (userDto.isModeration()) {
      int count = postRepository.findModerationPosts().size();
      userDto.setModerationCount(count);
      return;
    }
    userDto.setModerationCount(0);
  }

  private void setSettingsAndModerationStatus(User user, UserDto userDto) {
    if (user.getIsModerator() == 1) {
      userDto.setModeration(true);
      userDto.setSettings(true);
      return;
    }
    userDto.setSettings(false);
    userDto.setModeration(false);
  }

}