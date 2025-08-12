package com.Ghibli.GhibliAPI.service;

import com.Ghibli.GhibliAPI.client.StabilityAIClient;
import com.Ghibli.GhibliAPI.dto.TextToImageRequest;
import com.Ghibli.GhibliAPI.util.MultipartFileWrapper;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class GhibliArtService {

    private final StabilityAIClient stabilityAIClient;
    private final String apiKey;

    // Allowed dimensions from Stability AI docs
    private static final int[][] ALLOWED_DIMENSIONS = {
            {1024, 1024},
            {1152, 896},
            {1216, 832},
            {1344, 768},
            {1536, 640},
            {640, 1536},
            {768, 1344},
            {832, 1216},
            {896, 1152}
    };

    public GhibliArtService(StabilityAIClient stabilityAIClient, @Value("${stability.api.key}") String apiKey) {
        this.stabilityAIClient = stabilityAIClient;
        this.apiKey = apiKey;
    }

    // Resize image to the closest allowed dimensions
    private byte[] resizeImageToAllowedDimensions(MultipartFile file) throws IOException {
        BufferedImage img = ImageIO.read(file.getInputStream());
        int width = img.getWidth();
        int height = img.getHeight();

        // If already allowed, return original bytes
        for (int[] dim : ALLOWED_DIMENSIONS) {
            if (width == dim[0] && height == dim[1]) {
                return file.getBytes();
            }
        }

        // Find the closest allowed size (based on aspect ratio difference)
        double aspectRatio = (double) width / height;
        int targetWidth = ALLOWED_DIMENSIONS[0][0];
        int targetHeight = ALLOWED_DIMENSIONS[0][1];
        double minDiff = Double.MAX_VALUE;

        for (int[] dim : ALLOWED_DIMENSIONS) {
            double allowedAspect = (double) dim[0] / dim[1];
            double diff = Math.abs(aspectRatio - allowedAspect);
            if (diff < minDiff) {
                minDiff = diff;
                targetWidth = dim[0];
                targetHeight = dim[1];
            }
        }

        // Resize to chosen allowed dimensions
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(img)
                .size(targetWidth, targetHeight)
                .keepAspectRatio(false) // we must force exact allowed size
                .outputFormat("png")
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }

    private MultipartFile toMultipartFile(byte[] bytes, String originalFilename, String contentType) {
        return new MultipartFileWrapper(bytes, originalFilename, contentType);
    }

    public byte[] createGhibliArt(MultipartFile image, String prompt) throws IOException {
        byte[] resizedImageBytes = resizeImageToAllowedDimensions(image);
        MultipartFile resizedMultipartImage = toMultipartFile(resizedImageBytes, image.getOriginalFilename(), image.getContentType());

        String finalPrompt = prompt + ", in the beautiful detailed anime style of Studio Ghibli.";
        String engineId = "stable-diffusion-xl-1024-v1-0";
        String stylePreset = "anime";

        return stabilityAIClient.genrateImageFromImage(
                "Bearer " + apiKey,
                engineId,
                resizedMultipartImage,
                finalPrompt,
                stylePreset
        );
    }

    public byte[] createGhibliArtFromText(String prompt, String style) {
        String finalPrompt = prompt + ", in the beautiful detailed anime style of Studio Ghibli.";
        String engineId = "stable-diffusion-xl-1024-v1-0";
        String stylePreset = style.equals("general") ? "anime" : style.replace("_", "-");

        // Pick one of the allowed resolutions for text-to-image (e.g., 1024x1024)
        int width = 1024;
        int height = 1024;

        TextToImageRequest requestPayLoad = new TextToImageRequest(finalPrompt, stylePreset, width, height);
        return stabilityAIClient.genrateImageFromText(
                "Bearer " + apiKey,
                engineId,
                requestPayLoad
        );
    }
}
