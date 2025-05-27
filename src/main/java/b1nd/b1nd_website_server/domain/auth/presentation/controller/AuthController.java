package b1nd.b1nd_website_server.domain.auth.presentation.controller;

import b1nd.b1nd_website_server.domain.auth.presentation.dto.request.LoginDto;
import b1nd.b1nd_website_server.domain.auth.presentation.dto.request.ReProviderDto;
import b1nd.b1nd_website_server.domain.auth.presentation.dto.response.ReProvideToken;
import b1nd.b1nd_website_server.domain.auth.presentation.dto.response.TokenDto;
import b1nd.b1nd_website_server.domain.auth.service.AuthService;
import b1nd.b1nd_website_server.domain.user.domain.entity.User;
import b1nd.b1nd_website_server.global.libs.jwt.JwtUtil;
import b1nd.b1nd_website_server.global.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth Api")
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "login", description = "dauth로그인")
    @PostMapping("/login")
    public ResponseData<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
        TokenDto tokenDto = authService.getToken(loginDto);
        return ResponseData.of(HttpStatus.OK, "로그인 성공", tokenDto);
    }
    @Operation(summary = "refresh", description = "토큰 Refresh")
    @PostMapping(value = "/refresh")
    public ResponseData<ReProvideToken> reProvider(@RequestBody @Valid ReProviderDto reProviderDto) {
        User user = jwtUtil.getUserByToken(reProviderDto.getRefreshToken());
        ReProvideToken reProvideToken = authService.reProvideToken(user.getEmail(), user.getRole());
        return ResponseData.of(HttpStatus.OK, "refresh 성공", reProvideToken);
    }


}
