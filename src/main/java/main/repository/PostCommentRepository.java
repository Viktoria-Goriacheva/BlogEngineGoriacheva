package main.repository;

import java.util.List;
import main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

  @Query("FROM PostComment p WHERE post.id = :id")
  List<PostComment> findCommentsForPostId(Integer id);
  @Query(value = "SELECT * FROM post_comments ORDER BY id DESC LIMIT 1", nativeQuery = true)
  PostComment findByIdOrderByIdDesc();
}
