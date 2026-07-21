package com.one_love_international_club.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final Cloudinary cloudinary;

    public Map<String, String> uploadMultipart(MultipartFile file, String folder) {
        return performUpload(file, folder);
    }

    public boolean deleteFile(String publicId) {
        try {
            Map<?, ?> deleteResult = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );

            String result = (String) deleteResult.get("result");
            boolean success = "ok".equals(result);

            if (success) {
                log.info("File deleted successfully from Cloudinary with public ID: {}", publicId);
            } else {
                log.warn("Failed to delete file from Cloudinary: {}", result);
            }

            return success;
        } catch (IOException e) {
            log.error("Failed to delete file from Cloudinary: {}", e.getMessage());
            return false;
        }
    }

    public Map<String, String> uploadBase64Image(String base64Image, String folder) {
        return performUpload(base64Image, folder);
    }

    private Map<String, String> performUpload(Object file, String folder) {

        String publicId = UUID.randomUUID().toString();
        Map<String, String> properties = new HashMap<>();
        Map<?, ?> uploadResult;
        try {

            if (file instanceof MultipartFile multipartFile) {

                uploadResult = cloudinary.uploader().upload(
                        multipartFile.getBytes(),
                        ObjectUtils.asMap(
                                "public_id", publicId,
                                "folder", folder,
                                "resource_type", "auto"
                        )
                );
            } else {
                String base64Image = (String) file;
                uploadResult = cloudinary.uploader().upload(
                        base64Image.getBytes(),
                        ObjectUtils.asMap(
                                "public_id", publicId,
                                "folder", folder,
                                "resource_type", "auto"
                        )
                );

            }

            log.info("File uploaded successfully to Cloudinary with public ID: {}", publicId);
            log.info("Upload file URl =====> {}", uploadResult.get("secure_url"));

            String fileUrl = (String) uploadResult.get("secure_url");

            properties.put("fileUrl", fileUrl);
            properties.put("publicId", folder + "/" + publicId);
            return properties;
        } catch (Exception e) {
            log.error("Failed to upload file to Cloudinary: {}", e.getMessage());
            throw new ClubException(ErrorCode.VALIDATION_ERROR, "Failed to upload file", e);
        }

    }

}
