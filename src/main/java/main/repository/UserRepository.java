package main.repository;

import main.dto.UserDTOForPost;
import main.dto.UserDTOForPostId;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

  @Query(value = "SELECT id,name,photo FROM users where users.id=:id ", nativeQuery = true)
  User findByIdUserForComment(Integer id);//UserDTOForPostId
  @Query(value = "SELECT id,name FROM users where users.id=:id ", nativeQuery = true)
  User findByIdUserForPostId(Integer id);//UserDTOForPost
  @Query(value = "SELECT * FROM users where users.id=:id ", nativeQuery = true)
  User findByIdUserId(Integer id);
}
