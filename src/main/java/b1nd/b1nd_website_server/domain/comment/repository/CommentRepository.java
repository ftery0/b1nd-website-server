package b1nd.b1nd_website_server.domain.comment.repository;

import b1nd.b1nd_website_server.domain.comment.domain.entity.Comment;
import b1nd.b1nd_website_server.domain.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
