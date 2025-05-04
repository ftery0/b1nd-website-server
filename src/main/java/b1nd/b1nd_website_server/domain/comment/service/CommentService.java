package b1nd.b1nd_website_server.domain.comment.service;

import b1nd.b1nd_website_server.domain.comment.domain.entity.Comment;
import b1nd.b1nd_website_server.domain.comment.presentation.dto.request.CreateCommentRequest;
import b1nd.b1nd_website_server.domain.comment.repository.CommentRepository;
import b1nd.b1nd_website_server.domain.post.domain.entity.Post;
import b1nd.b1nd_website_server.domain.post.repository.PostRepository;
import b1nd.b1nd_website_server.domain.user.domain.entity.User;
import b1nd.b1nd_website_server.domain.user.domain.enums.Role;
import b1nd.b1nd_website_server.global.libs.jwt.JwtUtil;
import b1nd.b1nd_website_server.global.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    // 댓글 등록
    public ResponseData<Long> createComment(CreateCommentRequest request, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .post(post)
                .content(request.content())
                .authorName(request.authorName())
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return ResponseData.of(HttpStatus.CREATED, "댓글 등록 성공", savedComment.getId());
    }

    //댓글조회
    public ResponseData<List<Comment>> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        List<Comment> comments = commentRepository.findByPost(post);
        return ResponseData.of(HttpStatus.OK, "댓글 조회 성공", comments);
    }

    //댓글 삭제 (권한:어드민)
    public ResponseData<String> deleteComment(Long commentId, String accessToken) {
        User user = jwtUtil.getUserByToken(accessToken);

        if (user.getRole() != Role.ADMIN) {
            return ResponseData.of(HttpStatus.FORBIDDEN, "관리자만 댓글을 삭제할 수 있습니다.", null);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        commentRepository.delete(comment);
        return ResponseData.of(HttpStatus.OK, "댓글 삭제 성공", "댓글 ID: " + commentId);
    }

}
