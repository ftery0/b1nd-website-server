package b1nd.b1nd_website_server.domain.auth.presentation.dto.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenRes {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String expiresIn;
}
