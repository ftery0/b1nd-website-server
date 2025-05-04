package b1nd.b1nd_website_server.domain.post.presentation.controller;

import b1nd.b1nd_website_server.domain.post.presentation.dto.PostDto;
import b1nd.b1nd_website_server.domain.post.presentation.dto.request.PostRequestDto;
import b1nd.b1nd_website_server.domain.post.presentation.dto.response.PostResponse;
import b1nd.b1nd_website_server.domain.post.presentation.dto.request.PageRequest;
import b1nd.b1nd_website_server.domain.post.service.PostService;
import b1nd.b1nd_website_server.global.annotation.AuthCheck;
import b1nd.b1nd_website_server.global.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "Post Api")
@RestController
@RequestMapping(value = "/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "write Blog", description = "블로그 생성")
    @PostMapping
    @AuthCheck
    public ResponseData<Long> createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
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

    @Operation(summary = "대기 중인 블로그 목록", description = "PENDING 상태의 블로그 조회")
    @GetMapping("/list/pending")
    public ResponseData<PostResponse> getPendingPosts(
            @RequestParam(name = "page", defaultValue = "1") long page,
            @RequestParam(name = "size", defaultValue = "10") long size
    ) {
        return postService.getPendingPosts(new PageRequest(page, size));
    }

    @Operation(summary = "블로그 승인", description = "PENDING → ALLOWED 상태로 변경")
    @PatchMapping("/approve/{id}")
    public ResponseData<String> approvePost(@PathVariable Long id) {
        return postService.approvePost(id);
    }

    @Operation(summary = "블로그 상세페이지", description = "블로그 상세 확인 api")
    @GetMapping("/{id}")
    public ResponseData<PostDto> getPostDetail(@PathVariable("id") Long id) {
        return postService.getPostDetail(id);
    }

}
