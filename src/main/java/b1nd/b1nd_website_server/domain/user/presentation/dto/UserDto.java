package b1nd.b1nd_website_server.domain.user.presentation.dto;
import b1nd.b1nd_website_server.domain.user.domain.entity.User;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {
    private long  user_id;
    private String user_email;
    private String user_name;
    private String user_role;

    public static UserDto from(User user) {
        return UserDto.builder()
                .user_id(user.getId())
                .user_email(user.getEmail())
                .user_name(user.getName())
                .user_role(user.getRole().name())
                .build();
    }

}
