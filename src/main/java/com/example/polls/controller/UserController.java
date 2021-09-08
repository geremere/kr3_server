package com.example.polls.controller;

import com.example.polls.exception.AppException;
import com.example.polls.model.Amazon.Image;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserIdentityAvailability;
import com.example.polls.payload.UserSummary;
import com.example.polls.payload.response.UploadFileResponse;
import com.example.polls.payload.user.ChangePasswordDto;
import com.example.polls.payload.user.RegTypeDto;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.CurrentUser;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.AWSImageService;
import com.example.polls.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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
    private UserService userService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), currentUser.getImage(), currentUser.getEmail());
        return userSummary;
    }

    @GetMapping("/users/all")
    @PreAuthorize("hasRole('USER')")
    public List<UserSummary> getAllUser(@CurrentUser UserPrincipal currentUser) {
        List<UserSummary> allUsers = userRepository.findAll().stream()
                .filter(user -> user.getId() != currentUser.getId())
                .map(user -> UserSummary.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .name(user.getName())
                        .image(user.getImage())
                        .build())
                .collect(Collectors.toList());
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

    @PostMapping("/user/me/image")
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public UploadFileResponse setUserImage(@CurrentUser UserPrincipal currentUser,
                                           @RequestParam("file") MultipartFile file) throws IOException {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User didnt find in setUserIMage"));
        if (user.getImage() != null)
            imageService.deleteImage(user.getImage());
        Image image = imageService.store(file);
        user.setImage(image);

        userRepository.save(user);

        return new UploadFileResponse(image.getUrl(), image.getType(), file.getSize(), image.getImageId());
    }

    @GetMapping(value = {"/search/", "/search/{substr}"})
    @PreAuthorize("hasRole('USER')")
    public List<UserSummary> search(@PathVariable(required = false) String substr,
                                    @CurrentUser UserPrincipal currentUser) {

        if (substr == null) {
            return userRepository.findAll().stream()
                    .filter(user -> !user.getId().equals(currentUser.getId()))
                    .map(user -> UserSummary.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .name(user.getName())
                            .image(user.getImage())
                            .build())
                    .collect(Collectors.toList());
        }
        return userService.search(substr, currentUser.getId()).stream().map(user -> UserSummary.builder()
                .id(user.getId())
                .image(user.getImage())
                .username(user.getUsername())
                .name(user.getName())
                .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    public UserSummary getUserById(@PathVariable Long userId) {
        User user = userRepository.findById(userId).get();
        return UserSummary.builder()
                .id(userId)
                .name(user.getName())
                .username(user.getUsername())
                .image(user.getImage())
                .build();
    }

    @GetMapping("/user/isDefault")
    @PreAuthorize("hasRole('USER')")
    public RegTypeDto getRegTypes(@CurrentUser UserPrincipal currentUser){
        return userService.getRegTypes(currentUser.getId());
    }

    @PostMapping("/user/password")
    @PreAuthorize("hasRole('USER')")
    public boolean changePassword(@RequestBody ChangePasswordDto changePasswordDto,
                                  @CurrentUser UserPrincipal currentUser) {
        return userService.changePassword(changePasswordDto,currentUser.getId());
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('USER')")
    public boolean update(@PathVariable(required = true) Long id,
                          @RequestBody UserSummary userSummary){
        userService.update(id,userSummary);
        return true;
    }

}
