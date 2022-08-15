package main.repository;

import java.time.LocalDate;
import java.util.List;
import main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  @Query(value = "SELECT * FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED' AND time <= NOW()", nativeQuery = true)
  List<Post> findAllPost();

  @Query(value = "SELECT * FROM posts WHERE time= :date AND is_active = 1 AND moderation_status = 'ACCEPTED' AND time <= NOW()", nativeQuery = true)
  List<Post> findByDate(LocalDate date);

  @Query(value = "SELECT "
      + "    posts.id, "
      + "    posts.is_active, "
      + "    posts.moderation_status, "
      + "    posts.moderator_id, "
      + "    posts.text, "
      + "    posts.time, "
      + "    posts.title, "
      + "    posts.view_count, "
      + " posts.user_id, "
      + " tags.name "
      + "FROM "
      + "    posts "
      + "INNER JOIN tag2post ON tag2post.post_id =posts.id "
      + "INNER JOIN tags ON tag2post.tag_id = tags.id "
      + "where tags.name = :tag AND posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time <= NOW()", nativeQuery = true)
  List<Post> findByTag(String tag);

  @Query(value = "SELECT * FROM posts WHERE id= :id AND is_active = 1 AND moderation_status = 'ACCEPTED' AND time <= NOW()", nativeQuery = true)
  Post findByIdPost(Integer id);

  @Query(value = "SELECT * FROM posts WHERE LOWER(title) LIKE LOWER(CONCAT('%',?1,'%'))  AND is_active = 1 AND moderation_status = 'ACCEPTED' AND time <= NOW()", nativeQuery = true)
  List<Post> findAllByQuery(String path);

  @Query(value = "SELECT tags.name "
      + "FROM posts "
      + "INNER JOIN tag2post ON tag2post.post_id =posts.id "
      + "INNER JOIN tags ON tag2post.tag_id = tags.id where posts.id =:id AND posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time <= NOW()", nativeQuery = true)
  List<String> findTagsList(Integer id);

  @Query(value = "SELECT user_id "
      + "FROM posts "
      + "WHERE posts.id =:id AND posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time <= NOW()", nativeQuery = true)
  Integer findIdUser(Integer id);

  @Query(value = "SELECT * FROM posts WHERE moderation_status = 'NEW'", nativeQuery = true)
  List<Post> findModerationPosts();
}
