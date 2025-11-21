package com.ecommerce.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImg(String path, MultipartFile file) throws IOException {

        // Validate file name
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename(), "Invalid file");

        // Generate unique file name
        // mat.jpg -> 21321.jpg
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId + originalFileName.substring(originalFileName.lastIndexOf('.'));

        // Full file path
        String filePath = path + File.separator + fileName;

        // Create folder if not exists
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs(); // fixed
        }

        // Upload the file
        Files.copy(
                file.getInputStream(),
                Paths.get(filePath),
                StandardCopyOption.REPLACE_EXISTING // fixed
        );

        // Return generated file name
        return fileName;
    }

}
