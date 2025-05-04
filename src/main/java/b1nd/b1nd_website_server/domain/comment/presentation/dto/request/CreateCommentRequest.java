package b1nd.b1nd_website_server.domain.comment.presentation.dto.request;

public record CreateCommentRequest(
        String content,
        String authorName
){}
