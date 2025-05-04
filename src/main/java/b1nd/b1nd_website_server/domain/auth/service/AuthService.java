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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final UserService userService;
    private final DodamWebClientTemplate webClientTemplate;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);


    public void logAllFields(Object object) {
        try {

            Class<?> clazz = object.getClass();


            Field[] fields = clazz.getDeclaredFields();


            for (Field field : fields) {
                field.setAccessible(true);


                log.info("[getUserInfo] {}: {}", field.getName(), field.get(object));
            }
        } catch (IllegalAccessException e) {
            log.error("Error accessing field: ", e);
        }
    }


    public TokenDto getToken(LoginDto loginDto) {
        System.out.println(loginDto.getCode());
        String accessToken = webClientTemplate.auth(loginDto.getCode(), "/token");

        System.out.println(accessToken);
        return getUserInfo(accessToken);
    }

    public TokenDto getUserInfo(String accessToken) {
        log.info("[getUserInfo] 사용자 정보 요청 - accessToken: {}", accessToken);


        UserInfoDto infoDto = webClientTemplate.openApi(accessToken, "/user").getData();


        logAllFields(infoDto);

        log.info("[getUserInfo] 사용자 정보 수신 - user: {}", infoDto);


        User user = userService.save(User.builder()
                .email(infoDto.getEmail())
                .name(infoDto.getName())
                .role(Role.STUDENT)
                .build());

        log.info("[getUserInfo] 사용자 저장 완료 - userId: {}, email: {}", user.getId(), user.getEmail());
        return createUserToken(user.getEmail(), user.getRole());
    }

    private TokenDto createUserToken(String email, @NotNull Role role) {
        log.info("[createUserToken] 토큰 생성 시작 - email: {}, role: {}", email, role);
        TokenDto tokenDto = TokenDto.builder()
                .accessToken(tokenService.generateAccessToken(email, role))
                .refreshToken(tokenService.generateRefreshToken(email, role))
                .build();
        System.out.println(tokenDto.getAccessToken());
        log.info("[createUserToken] 토큰 생성 완료 - accessToken: {}, refreshToken: {}", tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        return tokenDto;
    }

    public ReProvideToken reProvideToken(String email, @NotNull Role role) {
        String accessToken = tokenService.generateAccessToken(email, role);
        return ReProvideToken.builder()
                .accessToken(accessToken)
                .build();
    }

}
