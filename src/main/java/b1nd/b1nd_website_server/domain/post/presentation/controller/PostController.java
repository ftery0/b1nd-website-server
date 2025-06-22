package b1nd.b1nd_website_server.domain.post.presentation.controller;

import b1nd.b1nd_website_server.domain.post.presentation.dto.PostDto;
import b1nd.b1nd_website_server.domain.post.presentation.dto.request.PostRequestDto;
import b1nd.b1nd_website_server.domain.post.presentation.dto.response.PostResponse;
import b1nd.b1nd_website_server.domain.post.presentation.dto.request.PageRequest;
import b1nd.b1nd_website_server.domain.post.service.PostService;
import b1nd.b1nd_website_server.domain.user.domain.enums.Role;
import b1nd.b1nd_website_server.global.annotation.AuthCheck;
import b1nd.b1nd_website_server.global.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "Post Api")
@RestController
@RequestMapping(value = "/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @Operation(summary = "write Blog", description = "블로그 생성")
    @PostMapping
    @AuthCheck(roles = {Role.ADMIN,Role.STUDENT})
    public ResponseData<Long> createPost(@RequestBody PostRequestDto postRequestDto,
                                         @RequestHeader("Authorization") String token) {
        log.info("Received token: {}", token);

        String parsedToken = token.replace("Bearer", "").trim();
        log.info("Parsed token: {}", parsedToken);

        return postService.createPost(postRequestDto, parsedToken);
    }

    @Operation(summary = "blog List", description = "블로그 불러오기")
    @GetMapping("/list")
    public ResponseData<PostResponse> getPost(
            @RequestParam(name = "page", defaultValue = "1") long page,
            @RequestParam(name = "size", defaultValue = "10") long size
            ) {
        PageRequest pageRequest = new PageRequest(page, size);

        return postService.getPosts(pageRequest);
    }

    @Operation(summary = "pending blog list", description = "PENDING 상태의 블로그 조회")
    @GetMapping("/list/pending")
    public ResponseData<PostResponse> getPendingPosts(
            @RequestParam(name = "page", defaultValue = "1") long page,
            @RequestParam(name = "size", defaultValue = "10") long size
    ) {
        return postService.getPendingPosts(new PageRequest(page, size));
    }

    @Operation(summary = "blog Approval", description = "PENDING → ALLOWED 상태로 변경")
    @PatchMapping("/approve/{id}")
    public ResponseData<String> approvePost(@PathVariable Long id) {
        return postService.approvePost(id);
    }

    //애들아 나중에 이거 이메일로 "거절 돼었습니다 + 사유" 메시지 전송해주는 기능 있으면 좋을거 같아
    //거절이지만 삭제하는 느낌스..
    @Operation(summary = "blog reject", description = "게시글 삭제")
    @PatchMapping("/reject/{id}")
    public ResponseData<String> rejectPost(@PathVariable Long id) {
        return postService.rejectPost(id);
    }

    @Operation(summary = "blog detail page", description = "블로그 상세 확인 api")
    @GetMapping("/{id}")
    public ResponseData<PostDto> getPostDetail(@PathVariable("id") Long id) {
        return postService.getPostDetail(id);
    }

}
