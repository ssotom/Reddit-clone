package ssotom.clone.reddit.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.User;
import ssotom.clone.reddit.demo.model.Vote;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User currentUser);

}
