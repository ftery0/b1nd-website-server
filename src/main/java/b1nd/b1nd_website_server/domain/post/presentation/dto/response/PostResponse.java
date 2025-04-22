package b1nd.b1nd_website_server.domain.post.presentation.dto.response;

import b1nd.b1nd_website_server.domain.post.presentation.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponse {
    private int totalPage;
    private List<PostDto> data;
}
