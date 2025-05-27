package b1nd.b1nd_website_server.domain.auth.presentation.dto.api;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoDto {
    private int grade;
    private int room;
    private int number;
    private String name;
    private String profileImage;
    private String email;
}
