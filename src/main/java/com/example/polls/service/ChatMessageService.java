package com.example.polls.service;

import com.example.polls.exception.AppException;
import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.chat.*;
import com.example.polls.model.user.User;
import com.example.polls.payload.requests.chat.ChatMessageRequest;
import com.example.polls.payload.response.MessageResponse;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.chat.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final MessageStatusRepository messageStatusRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageContentTypeRepository messageContentTypeRepository;
    private final MessageContentRepository messageContentRepository;

    @Transactional
    public ChatMessage save(ChatMessageRequest chatMessage) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatMessage);

        MessageStatus statusR = messageStatusRepository.findByName(MessageStatusName.RECEIVED).get();
        MessageContentType contentType = messageContentTypeRepository.findByType(MessageContentTypeEnum.valueOf(chatMessage.getType())).get();
        MessageContent messageContent = MessageContent
                .builder()
                .content(chatMessage.getContent())
                .type(contentType)
                .build();
        messageContentRepository.save(messageContent);
        ChatMessage chatMessageEntity = ChatMessage.builder()
                .content(messageContent)
                .senderId(chatMessage.getSenderId())
                .senderName(chatMessage.getSenderName())
                .chat(chatRoom)
                .status(statusR)
                .build();
        chatMessageRepository.save(chatMessageEntity);
        return chatMessageEntity;
    }


    public List<MessageResponse> findChatMessages(Long chatId) {

        List<ChatMessage> messages = chatRoomRepository.findById(chatId).get().getChatMessages();
        final MessageStatus statusD = messageStatusRepository.findByName(MessageStatusName.DELIVERED).get();
        final MessageStatus statusR = messageStatusRepository.findByName(MessageStatusName.RECEIVED).get();
        List<MessageResponse> response = new ArrayList<>();
        for (ChatMessage mess : messages) {
            if (mess.getStatus().getName().equals(MessageStatusName.RECEIVED)) {
                statusR.removeMessage(mess);
                statusD.addMessage(mess);
                chatMessageRepository.save(mess);
            }
            response.add(MessageResponse.builder()
                    .id(mess.getId())
                    .content(mess.getContent().getContent())
                    .type(mess.getContent().getType().getType())
                    .senderId(mess.getSenderId())
                    .senderName(mess.getSenderName())
                    .build());
        }
        return response;
    }

    public MessageResponse findById(Long id) {
        final MessageStatus statusR = messageStatusRepository.findByName(MessageStatusName.RECEIVED).get();
        MessageStatus statusD = messageStatusRepository.findByName(MessageStatusName.DELIVERED).get();
        return chatMessageRepository
                .findById(id)
                .map(chatMessage -> {
                    statusR.removeMessage(chatMessage);
                    statusD.addMessage(chatMessage);
                    ChatMessage saved = chatMessageRepository.save(chatMessage);
                    return MessageResponse.builder()
                            .id(saved.getId())
                            .content(saved.getContent().getContent())
                            .type(saved.getContent().getType().getType())
                            .senderId(saved.getSenderId())
                            .senderName(saved.getSenderName())
                            .build();
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("findById message", "id", id));
    }

    public Long countNewMessages(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).get();
        List<ChatMessage> messages = chatRoom.getChatMessages();
        Long counter = new Long(0);
        for (ChatMessage it : messages) {
            if(MessageStatusName.RECEIVED == it.getStatus().getName())
                counter++;
        }

        return counter;
    }

}