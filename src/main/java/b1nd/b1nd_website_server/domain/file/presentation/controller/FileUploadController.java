package b1nd.b1nd_website_server.domain.file.presentation.controller;

import b1nd.b1nd_website_server.domain.file.presentation.dto.response.FileResponsse;
import b1nd.b1nd_website_server.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Files", description = "Files Upload Api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponsse> uploadFile(@RequestPart("file") MultipartFile file) {
        FileResponsse response = fileService.uploadFile(file);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/upload")
//    public ResponseEntity<FileResponsse> uploadFile(
//            @RequestPart("file") MultipartFile file,
//            @RequestParam("postId") Long postId
//    ) {
//        FileResponsse response = fileService.uploadFile(file, postId);
//        return ResponseEntity.ok(response);
//    }
}
