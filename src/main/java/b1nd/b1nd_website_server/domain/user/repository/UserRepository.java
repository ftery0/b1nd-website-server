package b1nd.b1nd_website_server.domain.user.repository;

import b1nd.b1nd_website_server.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findById(String userId);
}
