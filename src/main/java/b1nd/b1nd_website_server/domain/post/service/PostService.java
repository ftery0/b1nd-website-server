package b1nd.b1nd_website_server.domain.post.service;

import b1nd.b1nd_website_server.domain.post.domain.entity.Post;
import b1nd.b1nd_website_server.domain.post.domain.enums.PostStatus;
import b1nd.b1nd_website_server.domain.post.presentation.dto.PostDto;
import b1nd.b1nd_website_server.domain.post.presentation.dto.request.PageRequest;
import b1nd.b1nd_website_server.domain.post.presentation.dto.request.PostRequestDto;
import b1nd.b1nd_website_server.domain.post.presentation.dto.response.PostResponse;
import b1nd.b1nd_website_server.domain.post.repository.PostRepository;

import b1nd.b1nd_website_server.global.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;


    public ResponseData<Long> createPost(PostRequestDto postDtoRequestDto) {
        Post post = Post.builder()
                .title(postDtoRequestDto.getPost_title())
                .content(postDtoRequestDto.getPost_content())
                .author(postDtoRequestDto.getPost_author())
                .createdAt(new Date())
                .user(null)
                .build();
        Post savedPost = postRepository.save(post);
        return ResponseData.of(HttpStatus.CREATED, "게시글 생성 성공", savedPost.getId());

    }

    //블로그 내역 불러오기
    public ResponseData<PostResponse> getPosts(PageRequest pageRequest) {
        List<Post> allPosts = postRepository.findAll().stream()
                .filter(post -> post.getStatus() == PostStatus.ALLOWED)
                .collect(Collectors.toList());

        int totalPage = (int) Math.ceil((double) allPosts.size() / pageRequest.size());

        List<PostDto> pagedPosts = allPosts.stream()
                .map(PostDto::from)
                .skip((pageRequest.page() - 1) * pageRequest.size())
                .limit(pageRequest.size())
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse(totalPage, pagedPosts);
        return ResponseData.of(HttpStatus.OK, "게시글 조회 성공", postResponse);
    }

    //대기중인 블로그 불러오기
    public ResponseData<PostResponse> getPendingPosts(PageRequest pageRequest) {
        List<Post> pendingPosts = postRepository.findAll().stream()
                .filter(post -> post.getStatus() == PostStatus.PENDING)
                .collect(Collectors.toList());

        int totalPage = (int) Math.ceil((double) pendingPosts.size() / pageRequest.size());

        List<PostDto> pagedPosts = pendingPosts.stream()
                .map(PostDto::from)
                .skip((pageRequest.page() - 1) * pageRequest.size())
                .limit(pageRequest.size())
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse(totalPage, pagedPosts);
        return ResponseData.of(HttpStatus.OK, "대기 중인 게시글 조회 성공", postResponse);
    }

    //블로그 수락
    public ResponseData<String> approvePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        post.setPostStatus(PostStatus.ALLOWED);
        return ResponseData.of(HttpStatus.OK, "게시글 승인 완료", "게시글 ID: " + postId);
    }


}
