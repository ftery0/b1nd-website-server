package b1nd.b1nd_website_server.global.libs.jwt;

import b1nd.b1nd_website_server.domain.user.domain.entity.User;
import b1nd.b1nd_website_server.domain.user.domain.enums.Role;
import b1nd.b1nd_website_server.domain.user.service.UserService;
import b1nd.b1nd_website_server.global.error.CustomError;
import b1nd.b1nd_website_server.global.error.ErrorCode;
import b1nd.b1nd_website_server.global.properties.JwtProperties;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private Key getSignKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(Long userId, String email, Long time, JwtType type, Role role) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("type", type);
        claims.put("role", role);
        claims.setSubject(String.valueOf(userId));

        Date now = new Date();

        String subject = String.valueOf(userId);

        System.out.println("Generating token with subject: " + subject);  // 디버그용

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + time))
                .signWith(getSignKey(jwtProperties.getSecret()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException, IllegalArgumentException, UnsupportedJwtException, MalformedJwtException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey(jwtProperties.getSecret()))
                    .build()
                    .parseClaimsJws(token.trim())
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw CustomError.of(ErrorCode.TOKEN_EXPIRED);
        } catch (IllegalArgumentException e) {
            throw CustomError.of(ErrorCode.TOKEN_NOT_PROVIDED);
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw CustomError.of(ErrorCode.INVALID_TOKEN);
        }
    }

    public JwtType checkTokenType(String token) {
        String tokenType = extractAllClaims(token).get("type", String.class);
        if ("REFRESH".equals(tokenType)) {
            return JwtType.REFRESH;
        } else {
            return JwtType.ACCESS;
        }
    }

    public User getUserByToken(String token) {
        String subject = extractAllClaims(token).getSubject();
        System.out.println("subject: " + subject);
        if (subject == null) {
            throw CustomError.of(ErrorCode.INVALID_TOKEN);
        }
        log.error("조건문 넘김");

        try {

            Long userId = Long.valueOf(subject);
            log.error("try문",userId);
            return userService.findById(userId);
        } catch (NumberFormatException e) {
            log.error("토큰 subject를 Long으로 변환하는 중 오류 발생: {}", e.getMessage(), e);
            throw CustomError.of(ErrorCode.INVALID_TOKEN);
        }
    }




}
