package com.example.polls.controller;

import com.example.polls.exception.AppException;
import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.Amazon.Image;
import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.user.User;
import com.example.polls.payload.*;
import com.example.polls.payload.response.UploadFileResponse;
import com.example.polls.repository.FileRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.UserPrincipal;
import com.example.polls.security.CurrentUser;
import com.example.polls.service.AWSImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AWSImageService imageService;

    @Autowired
    private FileRepository fileRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), currentUser.getImage());
        return userSummary;
    }

    @GetMapping("/users/all")
    @PreAuthorize("hasRole('USER')")
    public List<UserSummary> getAllUser(@CurrentUser UserPrincipal currentUser) {
        List<UserSummary> allUsers = userRepository.findAll().stream().map(user -> new UserSummary(user.getId(), user.getUsername(), user.getName(), user.getImage())).collect(Collectors.toList());
        return allUsers;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));


        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt());

        return userProfile;
    }


    @PostMapping("/user/me/image")
    @PreAuthorize("hasRole('USER')")
    public UploadFileResponse setUserImage(@CurrentUser UserPrincipal currentUser,
                                           @RequestParam("file") MultipartFile file) throws IOException {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User didnt find in setUserIMage"));
        if (user.getImage() != null)
            imageService.deleteImage(user.getImage());
        Image image = imageService.store(file);
        Image tmp = user.getImage();
        user.setImage(image);

        userRepository.save(user);
        fileRepository.delete(tmp);

        return new UploadFileResponse(image.getUrl(), image.getType(), file.getSize());
    }

    @GetMapping("/user/chats")
    @PreAuthorize("hasRole('USER')")
    public List<ChatRoom> getChats(@CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User didnt find in setUserIMage"));
        return user.getChatRooms();

    }
}
