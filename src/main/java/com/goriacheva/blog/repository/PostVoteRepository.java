package com.goriacheva.blog.repository;

import java.util.Optional;
import com.goriacheva.blog.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

  @Query(value = "select*from post_votes where user_id=:userId and post_id=:postId", nativeQuery = true)
  Optional<PostVote> findOptionalVoteByIdUserAndIdPost(Integer userId, Integer postId);

}
