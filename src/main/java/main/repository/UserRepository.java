package main.repository;

import java.util.Optional;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

  @Query(value = "SELECT id,name,photo FROM users where users.id=:id", nativeQuery = true)
  User findByIdUserForComment(Integer id);

  @Query(value = "SELECT * FROM users where users.id=:id", nativeQuery = true)
  User findByIdUserForPostId(Integer id);

  @Query(value = "SELECT * FROM users where users.id=:id ", nativeQuery = true)
  User findByIdUserId(Integer id);

  Optional<User> findByEmail(String email);
}
