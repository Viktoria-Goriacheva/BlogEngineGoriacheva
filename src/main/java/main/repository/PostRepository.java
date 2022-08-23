package main.repository;

import java.time.LocalDateTime;
import java.util.List;
import main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  @Query("FROM Post WHERE isActive = 1 AND moderationStatus = 'ACCEPTED' AND time <= NOW()")
  List<Post> findAllPost();

  @Query("FROM Post WHERE time= :date AND isActive = 1 AND moderationStatus = 'ACCEPTED' AND time <= NOW()")
  List<Post> findByDate(LocalDateTime date);

  @Query(value = "SELECT "
      + "    posts.id, "
      + "    posts.is_active, "
      + "    posts.moderation_status, "
      + "    posts.moderator_id, "
      + "    posts.text, "
      + "    posts.time, "
      + "    posts.title, "
      + "    posts.view_count, "
      + " posts.user_id "
      + "FROM "
      + "    posts "
      + "INNER JOIN tag2post ON tag2post.post_id =posts.id "
      + "INNER JOIN tags ON tag2post.tag_id = tags.id "
      + "where tags.name = :tag AND posts.is_active = 1 AND posts.moderation_status = 'ACCEPTED' AND posts.time <= NOW()", nativeQuery = true)
  List<Post> findByTag(String tag);

  @Query("FROM Post WHERE id= :id AND isActive = 1 AND moderationStatus = 'ACCEPTED' AND time <= NOW()")
  Post findByIdPost(Integer id);

  @Query("FROM Post WHERE LOWER(title) LIKE LOWER(CONCAT('%',?1,'%'))  AND isActive = 1 AND moderationStatus = 'ACCEPTED' AND time <= NOW()")
  List<Post> findAllByQuery(String path);

  @Query("SELECT user.id FROM Post p WHERE p.id =:id AND p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= NOW()")
  Integer findIdUser(Integer id);

  @Query("FROM Post p INNER JOIN User u ON u.id =p.user WHERE u.email = :email and p.isActive = 0")
  List<Post> findByInactive(String email);

  @Query("FROM Post p INNER JOIN User u ON u.id =p.user WHERE u.email = :email and p.isActive = 1 and p.moderationStatus = 'NEW'")
  List<Post> findByPending(String email);

  @Query("FROM Post p INNER JOIN User u ON u.id =p.user WHERE u.email = :email and p.isActive = 1 and p.moderationStatus = 'DECLINED'")
  List<Post> findByDeclined(String email);

  @Query("FROM Post p INNER JOIN User u ON u.id =p.user WHERE u.email = :email and p.isActive = 1 and p.moderationStatus = 'ACCEPTED'")
  List<Post> findByPublished(String email);

  @Query("FROM Post WHERE (moderatorId = :moderatorId or moderatorId is null) and isActive = 1 and moderationStatus = 'NEW'")
  List<Post> findByPendingMod(Integer moderatorId);

  @Query("FROM Post WHERE (moderatorId = :moderatorId or moderatorId is null) and isActive = 1 and moderationStatus = 'DECLINED'")
  List<Post> findByDeclinedMod(Integer moderatorId);

  @Query("FROM Post WHERE (moderatorId = :moderatorId or moderatorId is null) and isActive = 1 and moderationStatus = 'ACCEPTED'")
  List<Post> findByPublishedMod(Integer moderatorId);

  @Query(value = "FROM Post WHERE moderationStatus = 'NEW'")
  List<Post> findModerationPosts();
}
