package b1nd.b1nd_website_server.domain.comment.presentation.dto.response;

import b1nd.b1nd_website_server.domain.comment.domain.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentDto(
        Long commentId,
        String authorName,
        String content,
        LocalDateTime createdAt
) {
    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getId())
                .authorName(comment.getAuthorName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}