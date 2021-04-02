package com.example.polls.service;

import com.example.polls.exception.AppException;
import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.chat.ChatMessage;
import com.example.polls.model.chat.MessageStatus;
import com.example.polls.model.chat.MessageStatusName;
import com.example.polls.payload.requests.ChatMessageRequest;
import com.example.polls.payload.requests.MessageStatusRequest;
import com.example.polls.repository.ChatMessageRepository;
import com.example.polls.repository.MessageStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final MessageStatusRepository messageStatusRepository;

    @Transactional
    public ChatMessage save(ChatMessageRequest chatMessage) {
        MessageStatus status =  messageStatusRepository.findByName(MessageStatusName.RECEIVED).
                orElseThrow(() -> new AppException("some error with set status for message"));
//        MessageStatusRequest statusRequest = MessageStatusRequest.builder().id(status.getId()).name(status.getName()).build();
        ChatMessage chatMessageEntity = new ChatMessage();
        chatMessageEntity.setChatId(chatMessage.getChatId());
        chatMessageEntity.setContent(chatMessage.getContent());
        chatMessageEntity.setRecipientId(chatMessage.getRecipientId());
        chatMessageEntity.setRecipientName(chatMessage.getRecipientName());
        chatMessageEntity.setSenderId(chatMessage.getSenderId());
        chatMessageEntity.setSenderName(chatMessage.getSenderName());
        chatMessageEntity.setStatus(status);
        repository.save(chatMessageEntity);
        return chatMessageEntity;
    }

    public long countNewMessages(Long senderId, Long recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatusName.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<ChatMessage> messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        MessageStatus status = messageStatusRepository.findByName(MessageStatusName.DELIVERED).
                orElseThrow(() -> new AppException("some error with set status for message"));

        for (ChatMessage mess : messages) {
            mess.setStatus(status);
            repository.save(mess);
        }

        return messages;
    }

    public ChatMessage findById(Long id) {
        MessageStatus status = messageStatusRepository.findByName(MessageStatusName.DELIVERED).
                orElseThrow(() -> new AppException("some error with set status for message"));
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(status);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("findById message", "id", id));
    }
}