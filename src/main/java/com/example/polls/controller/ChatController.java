package com.example.polls.controller;


import com.example.polls.model.chat.ChatRoom;
import com.example.polls.payload.chat.ChatNotification;
import com.example.polls.payload.requests.chat.MessageSendDto;
import com.example.polls.payload.chat.ChatRoomDto;
import com.example.polls.security.CurrentUser;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.ChatRoomService;
import com.example.polls.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final UserService userService;


    @MessageMapping("/chat")
    public void sendMessage(@Payload MessageSendDto chatMessage) {
        ChatRoom chatRoom = chatRoomService.sendMessage(chatMessage, chatMessage.getSenderId());
        chatRoom.getUsers()
                .forEach(user-> messagingTemplate.convertAndSendToUser(
                            user.getId().toString(), "/queue/messages",
                            ChatNotification.builder()
                                    .sender(userService.toSummary(user))
                                    .chatId(chatRoom.getId())
                                    .build()));
    }

    @GetMapping("/chat/{recipientId}")
    @PreAuthorize("hasRole('USER')")
    public ChatRoomDto getChat(@PathVariable Long recipientId, @CurrentUser UserPrincipal currentUser){
        return chatRoomService.toDto(chatRoomService.getChat(recipientId,currentUser.getId()),currentUser.getId());
    }

    @GetMapping("/chat")
    @PreAuthorize("hasRole('USER')")
    public List<ChatRoomDto> getChats(@CurrentUser UserPrincipal currentUser){
        return chatRoomService.list(currentUser.getId()).stream()
                .map(chat-> chatRoomService.toDto(chat,currentUser.getId()))
                .collect(Collectors.toList());
    }


}
