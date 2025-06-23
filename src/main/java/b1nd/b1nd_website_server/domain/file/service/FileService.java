package b1nd.b1nd_website_server.domain.file.service;

import b1nd.b1nd_website_server.domain.file.domain.entity.File;
import b1nd.b1nd_website_server.domain.file.presentation.dto.response.FileResponsse;
import b1nd.b1nd_website_server.domain.file.repository.FileRepository;
import b1nd.b1nd_website_server.domain.post.domain.entity.Post;
import b1nd.b1nd_website_server.domain.post.service.PostService;
import b1nd.b1nd_website_server.global.config.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class FileService {

    private final S3Uploader s3Uploader;
    private final FileRepository fileRepository;
    private final PostService postService;

    private final String uploadDir = System.getProperty("user.dir") + "/uploads";


    public FileResponsse uploadFile(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storedFilename = UUID.randomUUID().toString() + extension;

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(storedFilename);
            file.transferTo(filePath.toFile());

            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String url = baseUrl + "/uploads/" + storedFilename;
            return new FileResponsse(url, originalFilename);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }

//    public FileResponsse uploadFile(MultipartFile file, Long postId) {
//        String uploadedUrl = s3Uploader.upload(file, "uploads");
//
//        Post post = postService.getPostById(postId);
//
//        File savedFile = File.builder()
//                .originalFilename(file.getOriginalFilename())
//                .storedFilename(uploadedUrl.substring(uploadedUrl.lastIndexOf("/") + 1))
//                .url(uploadedUrl)
//                .extension(getExtension(file.getOriginalFilename()))
//                .size(file.getSize())
//                .post(post)
//                .build();
//
//        fileRepository.save(savedFile);
//
//
//        return new FileResponsse(uploadedUrl, file.getOriginalFilename());
//
//    }

    public void deleteFilesByPost(Post post) {
        for (File file : post.getFiles()) {
            s3Uploader.delete(file.getStoredFilename(), "uploads");
            fileRepository.delete(file);
        }
    }

    private boolean checkIsImage(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg") ||
                lower.endsWith(".gif") || lower.endsWith(".svg") || lower.endsWith(".webp");
    }

    private boolean checkIsVideo(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".mp4") || lower.endsWith(".mov") || lower.endsWith(".webm");
    }

    private boolean checkIsDocument(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".log") || lower.endsWith(".md") || lower.endsWith(".docx") ||
                lower.endsWith(".pptx") || lower.endsWith(".xlsx") || lower.endsWith(".txt") ||
                lower.endsWith(".patch") || lower.endsWith(".pdf");
    }

    private boolean checkIsCompressed(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".zip") || lower.endsWith(".gz") || lower.endsWith(".tgz");
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public void deleteFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));

        s3Uploader.delete(file.getStoredFilename(), "uploads");
        fileRepository.delete(file);
    }

}
