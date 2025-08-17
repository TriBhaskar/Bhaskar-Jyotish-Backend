package com.anterka.bjyotish.service.file.cloud;

import com.anterka.bjyotish.constants.enums.FileTypeEnum;
import com.anterka.bjyotish.dto.file.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUtilityService {

    private final CloudinaryService cloudinaryService;

    /**
     * Get user profile image URL
     */
    public Optional<String> getUserProfileImageUrl(Long userId) {
        return cloudinaryService.getPrimaryFile("user", userId, FileTypeEnum.PROFILE_IMAGE)
                .map(FileUploadResponse::getCloudinarySecureUrl);
    }

    /**
     * Get user profile image thumbnail URL
     */
    public Optional<String> getUserProfileImageThumbnailUrl(Long userId, int width, int height) {
        return cloudinaryService.getPrimaryFile("user", userId, FileTypeEnum.PROFILE_IMAGE)
                .map(file -> cloudinaryService.getThumbnailUrl(file.getCloudinaryPublicId(), width, height));
    }

    /**
     * Get astrologer certification documents
     */
    public List<FileUploadResponse> getAstrologerCertifications(Long astrologerProfileId) {
        return cloudinaryService.getFilesByEntity("astrologer_profile", astrologerProfileId, FileTypeEnum.CERTIFICATION);
    }

    /**
     * Get birth chart image
     */
    public Optional<String> getBirthChartImageUrl(Long birthChartId) {
        return cloudinaryService.getPrimaryFile("birth_chart", birthChartId, FileTypeEnum.BIRTH_CHART_IMAGE)
                .map(FileUploadResponse::getCloudinarySecureUrl);
    }

    /**
     * Get product images
     */
    public List<FileUploadResponse> getProductImages(Long productId) {
        return cloudinaryService.getFilesByEntity("product", productId, FileTypeEnum.PRODUCT_IMAGE);
    }

    /**
     * Get optimized image URLs with different sizes
     */
    public Map<String, String> getOptimizedImageUrls(String publicId) {
        return Map.of(
                "thumbnail", cloudinaryService.getThumbnailUrl(publicId, 150, 150),
                "small", cloudinaryService.getThumbnailUrl(publicId, 300, 300),
                "medium", cloudinaryService.getThumbnailUrl(publicId, 600, 600),
                "large", cloudinaryService.getThumbnailUrl(publicId, 1200, 1200),
                "original", cloudinaryService.getOptimizedImageUrl(publicId, Map.of("quality", "auto", "format", "auto"))
        );
    }
}
