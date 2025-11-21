package com.ecommerce.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uploadImg(String path, MultipartFile file) throws IOException;
}
