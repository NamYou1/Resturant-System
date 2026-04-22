package com.saranaresturantsystem.common;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class FileHandler {
    private final Cloudinary cloudinary;
    public String uploadImage(MultipartFile imageFile, String folder) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "auto"
                    )
            );
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }
}
