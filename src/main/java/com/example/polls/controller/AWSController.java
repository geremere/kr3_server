package com.example.polls.controller;

import com.example.polls.model.Amazon.Image;
import com.example.polls.payload.response.UploadFileResponse;
import com.example.polls.service.AWSImageService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/file")
public class AWSController {

    private final AWSImageService imageService;

    @PostMapping("/image/create")
    @PreAuthorize("hasRole('USER')")
    public UploadFileResponse createImage(@RequestParam("image") MultipartFile file) throws IOException {
        Image image = imageService.store(file);
        return new UploadFileResponse(image.getUrl(), image.getType(), image.getImageId(), file.getSize());
    }
}
