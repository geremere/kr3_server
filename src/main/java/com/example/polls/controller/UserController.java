package com.example.polls.controller;

import com.example.polls.exception.AppException;
import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.Amazon.Image;
import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.chat.ChatRoomTypeEnum;
import com.example.polls.model.user.User;
import com.example.polls.payload.*;
import com.example.polls.payload.response.ChatRoomResponse;
import com.example.polls.payload.response.UploadFileResponse;
import com.example.polls.repository.FileRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.chat.ChatRoomRepository;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AWSImageService imageService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;


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
    public List<ChatRoomResponse> getChats(@CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new AppException("User didnt find in setUserIMage"));
        List<ChatRoomResponse> chats = new ArrayList<>();
        for (ChatRoom it : user.getChatRooms()) {
            if (it.getType().getType() == ChatRoomTypeEnum.DIALOG) {
                for (User u : it.getUsers())
                    if (!u.getId().equals(user.getId()))
                        chats.add(ChatRoomResponse.builder()
                                .title(u.getName())
                                .image(u.getImage())
                                .id(it.getId())
                                .usersId(it.getUsers().stream().filter(us -> !us.getId().equals(user.getId())).map(us -> us.getId()).collect(Collectors.toList()))
                                .build());
                    else if (it.getUsers().size() == 1)
                        chats.add(ChatRoomResponse.builder()
                                .title(u.getName())
                                .image(u.getImage())
                                .id(it.getId())
                                .usersId(it.getUsers().stream().filter(us -> !us.getId().equals(user.getId())).map(us -> us.getId()).collect(Collectors.toList()))
                                .build());

            } else
                chats.add(ChatRoomResponse.builder()
                        .title(it.getTitle())
                        .image(it.getImage())
                        .id(it.getId())
                        .usersId(it.getUsers().stream().filter(us -> !us.getId().equals(user.getId())).map(us -> us.getId()).collect(Collectors.toList()))
                        .build());

        }
        return chats;

    }

    @GetMapping("/search/{substr}")
    public List<UserSummary> Search(@PathVariable String substr) {
        Optional<List<User>> opt = userRepository.findByNameContaining(substr);
        List<User> resultC = new ArrayList<>();
        if (opt.isPresent())
            resultC = opt.get();
        opt = userRepository.findByNameStartingWith(substr);
        List<User> resultS = new ArrayList<>();
        if (opt.isPresent())
            resultS = opt.get();
        Set<User> result = new HashSet<>();
        result.addAll(resultC);
        result.addAll(resultS);
        return result.stream().map(user -> UserSummary.builder()
                .id(user.getId())
                .image(user.getImage())
                .username(user.getUsername())
                .name(user.getName())
                .build())
                .collect(Collectors.toList());
    }
}
