package b1nd.b1nd_website_server.domain.auth.presentation.controller;

import b1nd.b1nd_website_server.domain.auth.presentation.dto.request.LoginDto;
import b1nd.b1nd_website_server.domain.auth.presentation.dto.response.TokenDto;
import b1nd.b1nd_website_server.domain.auth.service.AuthService;
import b1nd.b1nd_website_server.global.libs.jwt.JwtUtil;
import b1nd.b1nd_website_server.global.response.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseData<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
        TokenDto tokenDto = authService.getToken(loginDto);
        return ResponseData.of(HttpStatus.OK, "login성공", tokenDto);
    }



}
