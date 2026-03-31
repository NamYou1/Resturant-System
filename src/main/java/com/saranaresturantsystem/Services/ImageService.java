package com.saranaresturantsystem.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class ImageService {
    private final Cloudinary cloudinary;


    public String uploadImage(MultipartFile imageFile, String folder) {
        try {
            // បង្កើត params និងកំណត់ folder ឱ្យមានសណ្តាប់ធ្នាប់
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "auto"
                    )
            );

            // ប្រើ secure_url ដើម្បីទទួលបាន https://
            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }
}
