package b1nd.b1nd_website_server.domain.comment.presentation.dto.response;

import java.util.List;

public record CommentListResponse(
        int totalCount,
        List<CommentDto> comments
) {
    public static CommentListResponse of(List<CommentDto> comments) {
        return new CommentListResponse(comments.size(), comments);
    }
}