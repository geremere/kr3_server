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
        Optional<String> chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);

        final MessageStatus statusR = messageStatusRepository.findByName(MessageStatusName.RECEIVED).
                orElseThrow(() -> new AppException("some error with set status for message"));

        chatMessage.setChatId(chatId.get());
        ChatMessage chatMessageEntity = new ChatMessage();
        chatMessageEntity.setChatId(chatMessage.getChatId());
        chatMessageEntity.setContent(chatMessage.getContent());
        chatMessageEntity.setRecipientId(chatMessage.getRecipientId());
        chatMessageEntity.setRecipientName(chatMessage.getRecipientName());
        chatMessageEntity.setSenderId(chatMessage.getSenderId());
        chatMessageEntity.setSenderName(chatMessage.getSenderName());
        statusR.addMessage(chatMessageEntity);
        repository.save(chatMessageEntity);
        return chatMessageEntity;
    }

    public long countNewMessages(Long senderId, Long recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatusName.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, true);

        List<ChatMessage> messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());
        final MessageStatus statusD = messageStatusRepository.findByName(MessageStatusName.DELIVERED).
                orElseThrow(() -> new AppException("some error with set status for message"));
        final MessageStatus statusR = messageStatusRepository.findByName(MessageStatusName.RECEIVED).
                orElseThrow(() -> new AppException("some error with set status for message"));

        for (ChatMessage mess : messages) {
            if(mess.getStatus().getName().equals(MessageStatusName.RECEIVED)) {
                statusR.removeMessage(mess);
                statusD.addMessage(mess);
                repository.save(mess);
            }
        }

        return messages;
    }

    public ChatMessage findById(Long id) {
        final MessageStatus statusR = messageStatusRepository.findByName(MessageStatusName.RECEIVED).
                orElseThrow(() -> new AppException("some error with set status for message"));
        MessageStatus statusD = messageStatusRepository.findByName(MessageStatusName.DELIVERED).
                orElseThrow(() -> new AppException("some error with set status for message"));
        return repository
                .findById(id)
                .map(chatMessage -> {
                    statusR.removeMessage(chatMessage);
                    statusD.addMessage(chatMessage);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("findById message", "id", id));
    }
}