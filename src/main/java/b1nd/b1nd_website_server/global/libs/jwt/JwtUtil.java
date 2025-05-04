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
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final UserService userService;


    private Key getSignKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, Long time, JwtType type, Role role) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("type", type);
        claims.put("role", role);

        Date now = new Date();

        return Jwts.builder()
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
                    .parseClaimsJws(token)
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
        String tokenType = extractAllClaims(token).get("type").toString();
        if ("REFRESH".equals(tokenType)) {
            return JwtType.REFRESH;
        } else {
            return JwtType.ACCESS;
        }
    }

    public User getUserByToken(String token) {
        Long userId = Long.valueOf(extractAllClaims(token).getSubject());
        return userService.findById(userId);
    }
}
