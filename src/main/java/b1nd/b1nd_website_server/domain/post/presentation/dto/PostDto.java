package b1nd.b1nd_website_server.domain.post.presentation.dto;

import b1nd.b1nd_website_server.domain.post.domain.entity.Post;
import b1nd.b1nd_website_server.domain.post.domain.enums.PostStatus;
import lombok.*;

import java.text.SimpleDateFormat;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {
    private Long post_id;
    private String post_title;
    private String post_content;
    private String post_author;
    private String post_created_at;
    private PostStatus post_status;

    public static PostDto from(Post post) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(post.getCreatedAt());

        return PostDto.builder()
                .post_id(post.getId())
                .post_title(post.getTitle())
                .post_content(post.getContent())
                .post_author(post.getUser().getName())
                .post_created_at(formattedDate)
                .post_status(post.getStatus())
                .build();
    }

}
