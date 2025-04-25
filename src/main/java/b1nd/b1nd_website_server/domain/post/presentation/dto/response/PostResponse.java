package b1nd.b1nd_website_server.domain.post.presentation.dto.response;

import b1nd.b1nd_website_server.domain.post.presentation.dto.PostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponse {
    @Schema(example = "2")
    private int totalPage;
    private List<PostDto> data;
}
