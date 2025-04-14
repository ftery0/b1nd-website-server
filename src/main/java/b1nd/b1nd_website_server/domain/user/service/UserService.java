package b1nd.b1nd_website_server.domain.user.service;

import b1nd.b1nd_website_server.domain.user.domain.entity.User;
import b1nd.b1nd_website_server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        Optional<User> byEmail = userRepository.findById(user.getId());
        return byEmail.orElseGet(() -> userRepository.save(user));
    }
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }
}
