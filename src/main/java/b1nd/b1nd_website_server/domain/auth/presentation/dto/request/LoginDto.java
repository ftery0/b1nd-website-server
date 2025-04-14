package b1nd.b1nd_website_server.domain.auth.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {
    @NotEmpty
    private String code;
}
