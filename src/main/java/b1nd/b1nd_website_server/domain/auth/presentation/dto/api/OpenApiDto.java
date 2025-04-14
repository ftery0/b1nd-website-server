package b1nd.b1nd_website_server.domain.auth.presentation.dto.api;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpenApiDto {
    private String message;
    private UserInfoDto data;
}
