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

        return createUserToken(user.getEmail(), user.getRole());
    }

    //유저 토큰
    private TokenDto createUserToken(String email, @NotNull Role role) {
        return TokenDto.builder()
                .accessToken(tokenService.generateAccessToken(email, role))
                .refreshToken(tokenService.generateRefreshToken(email, role))
                .build();
    }

    public ReProvideToken reProvideToken(String email, @NotNull Role role){
        return ReProvideToken.builder()
                .accessToken(tokenService.generateAccessToken(email, role))
                .build();
    }


}
