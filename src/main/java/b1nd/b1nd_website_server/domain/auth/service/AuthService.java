package b1nd.b1nd_website_server.domain.auth.service;

import b1nd.b1nd_website_server.domain.auth.presentation.dto.api.UserInfoDto;
import b1nd.b1nd_website_server.domain.auth.presentation.dto.request.LoginDto;
import b1nd.b1nd_website_server.domain.auth.presentation.dto.response.ReProvideToken;
import b1nd.b1nd_website_server.domain.auth.presentation.dto.response.TokenDto;
import b1nd.b1nd_website_server.domain.token.service.TokenService;
import b1nd.b1nd_website_server.domain.user.domain.entity.User;
import b1nd.b1nd_website_server.domain.user.domain.enums.Role;
import b1nd.b1nd_website_server.domain.user.service.UserService;
import b1nd.b1nd_website_server.global.libs.webclient.template.DodamWebClientTemplate;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final UserService userService;
    private final DodamWebClientTemplate webClientTemplate;


    public TokenDto getToken(LoginDto loginDto) {
        return getUserInfo(webClientTemplate.auth(loginDto.getCode(),"/token"));
    }

    public TokenDto getUserInfo(String accessToken) {
        UserInfoDto infoDto = webClientTemplate.openApi(accessToken, "/user").getData();

        User user = userService.save(User.builder()
                .email(infoDto.getEmail())
                .name(infoDto.getName())
                .role(Role.STUDENT)
                .build());
        return createUserToken(user.getId(), user.getEmail(), user.getRole());

    }

    private TokenDto createUserToken(Long userId, String email, @NotNull Role role) {
        return TokenDto.builder()
                .accessToken(tokenService.generateAccessToken(userId, email, role))
                .refreshToken(tokenService.generateRefreshToken(userId, email, role))
                .build();
    }


    public ReProvideToken reProvideToken(String email, @NotNull Role role) {
        User user = userService.findByEmail(email); // 또는 적절한 조회 메서드 사용
        String accessToken = tokenService.generateAccessToken(user.getId(), email, role);
        return ReProvideToken.builder()
                .accessToken(accessToken)
                .build();
    }


}
