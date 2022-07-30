package main.repository;

import main.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

}
