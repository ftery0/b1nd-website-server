package b1nd.b1nd_website_server.domain.post.service;

import b1nd.b1nd_website_server.domain.comment.service.CommentService;
import b1nd.b1nd_website_server.domain.post.domain.entity.Post;
import b1nd.b1nd_website_server.domain.post.domain.enums.PostStatus;
import b1nd.b1nd_website_server.domain.post.presentation.dto.PostDto;
import b1nd.b1nd_website_server.domain.post.presentation.dto.request.PageRequest;
import b1nd.b1nd_website_server.domain.post.presentation.dto.request.PostRequestDto;
import b1nd.b1nd_website_server.domain.post.presentation.dto.response.PostResponse;
import b1nd.b1nd_website_server.domain.post.repository.PostRepository;

import b1nd.b1nd_website_server.domain.user.domain.entity.User;
import b1nd.b1nd_website_server.domain.user.domain.enums.Role;
import b1nd.b1nd_website_server.global.libs.jwt.JwtUtil;
import b1nd.b1nd_website_server.global.libs.webclient.template.DodamWebClientTemplate;
import b1nd.b1nd_website_server.global.response.ResponseData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private final CommentService commentService;

    public ResponseData<Long> createPost(PostRequestDto postDtoRequestDto, String token) {
        log.info("Received token: {}", token);
        log.info("PostRequestDto: title={}, content={}", postDtoRequestDto.getPost_title(), postDtoRequestDto.getPost_content());

        User user = jwtUtil.getUserByToken(token);

        if (user == null) {
            log.error("User not found from token!");
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }

        log.info("User info: id={}, email={}", user.getId(), user.getEmail());

        Post post = Post.builder()
                .title(postDtoRequestDto.getPost_title())
                .content(postDtoRequestDto.getPost_content())
                .summary(postDtoRequestDto.getPost_summary())
                .posterImage(postDtoRequestDto.getPoster_image())
                .createdAt(new Date())
                .user(user)
                .status(PostStatus.PENDING)
                .build();


        Post savedPost = postRepository.save(post);
        log.info("Post saved with ID: {}", savedPost.getId());

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
        postRepository.save(post);

        return ResponseData.of(HttpStatus.OK, "게시글 승인 완료", "게시글 ID: " + postId);
    }

    //블로그 거절
    @Transactional
    public ResponseData<String> rejectPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        postRepository.delete(post);

        return ResponseData.of(HttpStatus.OK, "게시글 삭제 완료", "삭제된 게시글 ID: " + postId);
    }

    // 블로그 상세 조회
    public ResponseData<PostDto> getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        PostDto postDto = PostDto.from(post);

        return ResponseData.of(HttpStatus.OK, "게시글 상세 조회 성공", postDto);
    }
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }


    public ResponseData<String> deletePost(Long postId, String token) {
        User user = jwtUtil.getUserByToken(token);

        if (user == null || user.getRole() != Role.ADMIN) {
            return ResponseData.of(HttpStatus.FORBIDDEN, "관리자만 게시글을 삭제할 수 있습니다.", null);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        commentService.deleteCommentsByPost(post, user);
        postRepository.delete(post);

        return ResponseData.of(HttpStatus.OK, "게시글 삭제 및 관련 댓글 삭제 성공", "게시글 ID: " + postId);
    }

}
