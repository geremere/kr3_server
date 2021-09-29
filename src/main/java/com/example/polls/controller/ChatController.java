package com.example.polls.controller;


import com.example.polls.model.chat.ChatRoom;
import com.example.polls.payload.chat.ChatNotification;
import com.example.polls.payload.chat.ChatRoomDto;
import com.example.polls.payload.chat.ChatRoomShortDto;
import com.example.polls.payload.requests.chat.MessageSendDto;
import com.example.polls.security.CurrentUser;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.ChatRoomService;
import com.example.polls.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Tag(name = "Контроллер для работы с чатом")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final UserService userService;


    @MessageMapping("/chat")
    public void sendMessage(@Payload MessageSendDto chatMessage) {
        ChatRoom chatRoom = chatRoomService.sendMessage(chatMessage);
        chatRoom.getUsers().stream()
                .filter(user -> !user.getId()
                        .equals(chatMessage.getSenderId()))
                .forEach(user -> messagingTemplate
                        .convertAndSendToUser(
                                user.getId().toString(), "/queue/messages",
                                ChatNotification.builder()
                                        .sender(userService
                                                .toSummary(userService
                                                        .getById(chatMessage.getSenderId())))
                                        .chatName(chatRoom.getTitle())
                                        .content(chatMessage.getContent())
                                        .chatId(chatRoom.getId())
                                        .build()));
    }

    @GetMapping("/chat/user/{recipientId}")
    @PreAuthorize("hasRole('USER')")
    public ChatRoomDto getChat(@PathVariable Long recipientId, @CurrentUser UserPrincipal currentUser) {
        return chatRoomService.toDto(chatRoomService.getChat(recipientId, currentUser.getId()));
    }

    @GetMapping("/chat")
    @PreAuthorize("hasRole('USER')")
    public List<ChatRoomShortDto> getChats(@CurrentUser UserPrincipal currentUser) {
        return chatRoomService.list(currentUser.getId()).stream()
                .map(chatRoom -> chatRoomService.toShortDto(chatRoom, currentUser.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/chat/{chatId}")
    @PreAuthorize("hasRole('USER')")
    public ChatRoomDto getChat(@PathVariable Long chatId) {
        return chatRoomService.toDto(chatRoomService.findById(chatId));
    }


}
