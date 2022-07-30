package main.repository;

import java.util.List;
import main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED' AND time <= NOW() ", nativeQuery = true)
  List<Post> findAll();
}
