package b1nd.b1nd_website_server.domain.post.presentation.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequestDto {
    private String post_title;
    private String post_content;
    private String post_summary;
    private String poster_image;
}
