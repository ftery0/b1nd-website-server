package b1nd.b1nd_website_server.global.annotation;

import b1nd.b1nd_website_server.domain.user.domain.enums.Role;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    Role[] roles() default {Role.STUDENT, Role.ADMIN};
}