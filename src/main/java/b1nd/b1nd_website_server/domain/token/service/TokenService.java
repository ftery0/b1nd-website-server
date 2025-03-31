package b1nd.b1nd_website_server.domain.token.service;

import b1nd.b1nd_website_server.domain.user.domain.enums.Role;
import b1nd.b1nd_website_server.global.libs.jwt.JwtType;
import b1nd.b1nd_website_server.global.libs.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtUtil jwtUtil;

    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 3600 * 24 * 3; // 24시간
    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 3600 * 24 * 3 * 60; // 3일

    public String generateAccessToken(String email, @NotNull Role role) {
        return jwtUtil.generateToken(email, ACCESS_TOKEN_EXPIRE_TIME, JwtType.ACCESS, role);
    };

    public String generateRefreshToken(String email, @NotNull Role role) {
        return jwtUtil.generateToken(email, REFRESH_TOKEN_EXPIRE_TIME, JwtType.REFRESH, role);
    };
}
