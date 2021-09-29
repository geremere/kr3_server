package com.example.polls.service;

import com.example.polls.exception.AppException;
import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.chat.ChatMessage;
import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.payload.chat.ChatRoomShortDto;
import com.example.polls.payload.requests.chat.MessageSendDto;
import com.example.polls.payload.chat.ChatRoomDto;
import com.example.polls.payload.chat.MessageDto;
import com.example.polls.repository.chat.ChatRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository repository;
    private final UserService userService;


    public ChatRoom fromDto(ChatRoomDto chatRoomDto) {
        List<User> users = chatRoomDto.getUsers().stream()
                .map(user -> userService.getById(user.getId()))
                .collect(Collectors.toList());
        return ChatRoom.builder()
                .id(chatRoomDto.getId())
                .image(chatRoomDto.getImage())
                .title(chatRoomDto.getTitle())
                .users(users)
                .isDialog(chatRoomDto.getIsDialog())
                .build();
    }

    public ChatRoomDto toDto(ChatRoom chatRoom) {
        List<MessageDto> messagesDto = chatRoom.getChatMessages() != null ?
                chatRoom.getChatMessages().stream()
                        .sorted(Comparator.comparing(ChatMessage::getUpdatedAt))
                        .map(this::toDto)
                        .collect(Collectors.toList())
                : new ArrayList<>();
        return ChatRoomDto.builder()
                .isDialog(chatRoom.getIsDialog())
                .messages(messagesDto)
                .title(chatRoom.getTitle())
                .id(chatRoom.getId())
                .users(chatRoom.getUsers().stream()
                        .map(userService::toSummary)
                        .collect(Collectors.toList()))
                .image(chatRoom.getImage())
                .lastMessage(messagesDto.isEmpty() ? null
                        : messagesDto.get(messagesDto.size() - 1))
                .build();
    }

    public MessageDto toDto(ChatMessage chatMessage) {
        return MessageDto.builder()
                .sender(userService.toSummary(chatMessage.getSender()))
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .updatedAt(chatMessage.getUpdatedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .build();
    }

    public ChatRoomShortDto toShortDto(ChatRoom chatRoom, Long currentUserId) {
        MessageDto lastMessage = chatRoom.getChatMessages().stream()
                .reduce((f, s) -> f.getUpdatedAt().compareTo(s.getUpdatedAt()) > 0 ? f : s)
                .map(this::toDto)
                .orElse(null);
        Long countNewMessages = chatRoom.getChatMessages().stream()
                .filter(message -> !message.getSender().getId().equals(currentUserId))
                .filter(message -> !message.isDelivered())
                .count();
        return ChatRoomShortDto.builder()
                .id(chatRoom.getId())
                .isDialog(chatRoom.getIsDialog())
                .title(chatRoom.getTitle())
                .lastMessage(lastMessage)
                .image(chatRoom.getImage())
                .countNewMessage(countNewMessages)
                .users(chatRoom.getUsers().stream()
                        .map(userService::toSummary)
                        .collect(Collectors.toList()))
                .build();
    }

    public ChatRoom findById(Long id) {
        ChatRoom chatRoom = repository.findById(id);
        chatRoom.getChatMessages().forEach(message -> message.setDelivered(true));
        return repository.save(chatRoom);
    }

    @Transactional
    public ChatRoom getChat(Long recipientId, Long senderId) {
        if (repository.isChatExist(recipientId, senderId)) {
            return repository.findChatByUsersId(recipientId, senderId);
        } else {
            List<User> users = new ArrayList<>();
            users.add(userService.getById(recipientId));
            users.add(userService.getById(senderId));

            return repository.save(ChatRoom.builder()
                    .users(users)
                    .isDialog(false)
                    .build());
        }
    }

    public ChatRoom update(ChatRoom chatRoom) {
        ChatRoom oldChatRoom = repository.findById(chatRoom.getId());
        chatRoom.setId(oldChatRoom.getId());
        return repository.save(chatRoom);
    }

    @Transactional
    public ChatRoom sendMessage(MessageSendDto sendDto) {
        ChatRoom chatRoom = repository.findById(sendDto.getChatId());
        chatRoom.getChatMessages().add(ChatMessage.builder()
                .content(sendDto.getContent())
                .isDelivered(false)
                .sender(userService.getById(sendDto.getSenderId()))
                .build());
        return repository.save(chatRoom);

    }

    public List<ChatRoom> list(Long userId) {
        return userService.getById(userId).getChatRooms();
    }
}
