package b1nd.b1nd_website_server.domain.auth.presentation.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
