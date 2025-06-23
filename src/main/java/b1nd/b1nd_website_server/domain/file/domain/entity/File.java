package b1nd.b1nd_website_server.domain.file.domain.entity;


import b1nd.b1nd_website_server.domain.post.domain.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String storedFilename;
    private String url;
    private String extension;
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime uploadedAt;

    @Builder
    public File(String originalFilename, String storedFilename, String url, String extension, Long size, Post post) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.url = url;
        this.extension = extension;
        this.size = size;
        this.post = post;
        this.uploadedAt = LocalDateTime.now();
    }
}