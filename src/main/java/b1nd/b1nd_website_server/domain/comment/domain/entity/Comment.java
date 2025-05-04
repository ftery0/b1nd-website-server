package b1nd.b1nd_website_server.domain.comment.domain.entity;

import b1nd.b1nd_website_server.domain.post.domain.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(length = 100)
    private String content;

    private LocalDateTime createdAt;

    @Column(length = 20)
    private String authorName;

}
