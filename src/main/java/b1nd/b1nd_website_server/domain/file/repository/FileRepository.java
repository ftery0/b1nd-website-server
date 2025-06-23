package b1nd.b1nd_website_server.domain.file.repository;

import b1nd.b1nd_website_server.domain.file.domain.entity.File;
import b1nd.b1nd_website_server.domain.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByPost(Post post);
}