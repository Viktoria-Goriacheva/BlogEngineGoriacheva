package main.repository;

import java.util.Optional;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("FROM User u where u.id=:id")
  User findByIdUserForPostId(Integer id);

  Optional<User> findByEmail(String email);

  @Query("SELECT u.id FROM User u where email=:email")
  Integer findByIdUserForMod(String email);
}
