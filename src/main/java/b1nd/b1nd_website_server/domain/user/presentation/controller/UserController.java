package b1nd.b1nd_website_server.domain.user.presentation.controller;

import b1nd.b1nd_website_server.domain.user.domain.entity.User;
import b1nd.b1nd_website_server.domain.user.presentation.dto.UserDto;
import b1nd.b1nd_website_server.domain.user.service.UserService;
import b1nd.b1nd_website_server.global.annotation.AuthCheck;
import b1nd.b1nd_website_server.global.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Operation(summary = "my information", description = "자신의 정보 가져오기")
    @GetMapping("/my")
    @AuthCheck
    public ResponseData<UserDto> getUser(@RequestAttribute("userId") Long userId) {
        User user = userService.findById(userId);
        log.info(user.toString());
        log.info("[UserController] 조회된 유저: id={}, email={}, name={}, role={}",
                user.getId(), user.getEmail(), user.getName(), user.getRole());
        UserDto userInfo = UserDto.from(user);
        return ResponseData.of(HttpStatus.OK, "자신의 정보 가져오기 성공", userInfo);
    }

}
