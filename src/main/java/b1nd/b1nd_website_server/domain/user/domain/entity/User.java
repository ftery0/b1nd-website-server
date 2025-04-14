package b1nd.b1nd_website_server.domain.user.domain.entity;

import b1nd.b1nd_website_server.domain.user.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;



@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {


    @Id
    @Column(name = "user_id")
    private String id;

    @NotNull
    @Column(name = "user_email")
    private String email;

    @Column(name = "user_name")
    @NotNull
    private String name;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Builder
    public User(String email, String name, Role role) {
        this.email = email;
        this.name = name;
        this.role = role;
    }
}
