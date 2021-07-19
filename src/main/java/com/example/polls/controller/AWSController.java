package com.example.polls.controller;

import com.example.polls.exception.AppException;
import com.example.polls.model.Amazon.Image;
import com.example.polls.model.user.User;
import com.example.polls.payload.response.UploadFileResponse;
import com.example.polls.security.CurrentUser;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.AWSImageService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("/api/image")
public class AWSController {

    private final AWSImageService imageService;
    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public UploadFileResponse saveImage(@RequestParam("file") MultipartFile file) throws IOException {
        Image image = imageService.store(file);
        return new UploadFileResponse(image.getUrl(), image.getType(), file.getSize());
    }
}
