package ssotom.clone.reddit.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssotom.clone.reddit.demo.model.Post;
import ssotom.clone.reddit.demo.model.Subreddit;
import ssotom.clone.reddit.demo.model.User;

import java.util.Arrays;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);

}
