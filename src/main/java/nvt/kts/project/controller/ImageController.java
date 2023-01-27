package nvt.kts.project.controller;

import lombok.RequiredArgsConstructor;
import nvt.kts.project.dto.ImageDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/image")
public class ImageController {

    @GetMapping(value = "/getImage/{name}")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String name) {
        try {
            FileSystemResource imgFile = new FileSystemResource("src/main/resources/static/images/" + name);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(imgFile.getInputStream()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/uploadImage")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<String> savePhoto(@RequestParam("image") MultipartFile image) {
        try {
            String name = "photo";
            Files.copy(image.getInputStream(), Paths.get("src\\main\\resources\\static").resolve("images").resolve(name + ".jpg").toAbsolutePath());
            return new ResponseEntity<>(name, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addImage")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<InputStreamResource> addImage(@RequestBody ImageDTO imageDTO) throws IOException {
        byte[] data;
        try {
            data = Base64.getDecoder().decode(imageDTO.getData().split(",")[1]);
        } catch(Exception e) {
            return null;
        }
        String imageName = imageDTO.getPath();
        String picturePath = "src\\main\\resources\\static\\images\\"+imageName;
        try (OutputStream stream = new FileOutputStream(new File(picturePath).getCanonicalFile())) {
            stream.write(data);
            FileSystemResource imgFile = new FileSystemResource("src/main/resources/static/images/" + imageName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(imgFile.getInputStream()));
        }

    }

}
