package com.example.polls.service;

import com.example.polls.exception.AppException;
import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.chat.*;
import com.example.polls.model.user.User;
import com.example.polls.payload.requests.chat.ChatMessageRequest;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.chat.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final MessageStatusRepository messageStatusRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageContentTypeRepository messageContentTypeRepository;
    private final UserRepository userRepository;
    private final MessageContentRepository messageContentRepository;

    @Transactional
    public ChatMessage save(ChatMessageRequest chatMessage) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatMessage.getChatId(),"DIALOG");
        User user = userRepository.findById(chatMessage.getRecipientId()).get();
        user.getChatRooms().add(chatRoom);
        User currentUser = userRepository.findById(chatMessage.getSenderId()).get();
        currentUser.getChatRooms().add(chatRoom);
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

//    public long countNewMessages(Long senderId, Long recipientId) {
//        return repository.countBySenderIdAndRecipientIdAndStatus(
//                senderId, recipientId, MessageStatusName.RECEIVED);
//    }

    public List<ChatMessage> findChatMessages(Long chatId) {

        List<ChatMessage> messages = chatRoomRepository.findById(chatId).get().getChatMessages();
        final MessageStatus statusD = messageStatusRepository.findByName(MessageStatusName.DELIVERED).get();
        final MessageStatus statusR = messageStatusRepository.findByName(MessageStatusName.RECEIVED).get();

        for (ChatMessage mess : messages) {
            if(mess.getStatus().getName().equals(MessageStatusName.RECEIVED)) {
                statusR.removeMessage(mess);
                statusD.addMessage(mess);
                chatMessageRepository.save(mess);
            }
        }
        return messages;
    }

    public ChatMessage findById(Long id) {
        final MessageStatus statusR = messageStatusRepository.findByName(MessageStatusName.RECEIVED).get();
        MessageStatus statusD = messageStatusRepository.findByName(MessageStatusName.DELIVERED).get();
        return chatMessageRepository
                .findById(id)
                .map(chatMessage -> {
                    statusR.removeMessage(chatMessage);
                    statusD.addMessage(chatMessage);
                    return chatMessageRepository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("findById message", "id", id));
    }


}