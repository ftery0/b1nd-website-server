package b1nd.b1nd_website_server.domain.auth.presentation.dto.api;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DAuthToken {
    private String code;
    private String client_id;
    private String client_secret;
}
