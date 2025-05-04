package b1nd.b1nd_website_server.domain.user.presentation.controller;

import b1nd.b1nd_website_server.domain.user.domain.entity.User;
import b1nd.b1nd_website_server.domain.user.presentation.dto.UserDto;
import b1nd.b1nd_website_server.domain.user.service.UserService;
import b1nd.b1nd_website_server.global.annotation.AuthCheck;
import b1nd.b1nd_website_server.global.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "User", description = "User Api")
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "my information", description = "자신의 정보 가져오기")
    @AuthCheck
    @GetMapping("/my")
    public ResponseData<UserDto> getUser(@RequestAttribute("userId") Long userId) {
        User user = userService.findById(userId);
        UserDto userInfo = UserDto.from(user);
        return ResponseData.of(HttpStatus.OK, "자신의 정보 가져오기 성공", userInfo);
    }

}
