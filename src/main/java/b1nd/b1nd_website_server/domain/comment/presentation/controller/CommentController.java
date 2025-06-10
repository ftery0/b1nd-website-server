package b1nd.b1nd_website_server.domain.comment.presentation.controller;

import b1nd.b1nd_website_server.domain.comment.presentation.dto.request.CreateCommentRequest;
import b1nd.b1nd_website_server.domain.comment.service.CommentService;
import b1nd.b1nd_website_server.global.response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Comment", description = "Comment Api")
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "create", description = "댓글 작성")
    @PostMapping("/create")
    public ResponseData<Long> createComment(
            @RequestBody CreateCommentRequest request,
            @RequestParam(name = "postId") Long postId
    ) {
        return commentService.createComment(request,postId);
    }

    @Operation(summary = "comment list", description = "댓글 불러오기")
    @GetMapping("/{postId}")
    public ResponseData<?> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @Operation(summary = "delete comment", description = "댓글 삭제(어드민만 가능)")
    @DeleteMapping("/{commentId}")
    public ResponseData<String> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String accessToken
    ) {
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7).trim();
        }
        return commentService.deleteComment(commentId, accessToken);
    }




}
