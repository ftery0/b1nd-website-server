package b1nd.b1nd_website_server.global.config.s3;
import b1nd.b1nd_website_server.global.properties.S3Properties;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final S3Properties s3Properties;

    public String upload(MultipartFile file, String dirName) {
        String originalFilename = file.getOriginalFilename();
        String storedFileName = dirName + "/" + UUID.randomUUID() + "_" + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            log.info("[S3Uploader] 파일 업로드 시작 - 파일명: {}", originalFilename);

            amazonS3.putObject(new PutObjectRequest(
                    s3Properties.getS3().getBucket(),
                    storedFileName,
                    file.getInputStream(),
                    metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));

            log.info("[S3Uploader] 파일 업로드 완료 - 저장된 경로: {}", storedFileName);

        } catch (IOException e) {
            log.error("[S3Uploader] 파일 업로드 실패", e);

            throw new RuntimeException("파일 업로드 실패", e);
        }

        String uploadedUrl = amazonS3.getUrl(s3Properties.getS3().getBucket(), storedFileName).toString();
        log.info("[S3Uploader] 접근 가능한 URL: {}", uploadedUrl);

        return amazonS3.getUrl(s3Properties.getS3().getBucket(), storedFileName).toString();
    }

    public void delete(String storedFileName, String dirName) {
        String key = dirName + "/" + storedFileName;
        amazonS3.deleteObject(s3Properties.getS3().getBucket(), key);
    }
}