package main.repository;

import java.util.List;
import main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {

  @Query(value = "select id,parent_id,reg_time,text,user_id "
      + "from post_comments "
      + "where post_id = :id",nativeQuery = true)
  List<PostComment> findCommentsForPostId(Integer id);
}
