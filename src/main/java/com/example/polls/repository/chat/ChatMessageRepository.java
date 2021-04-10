package com.example.polls.repository.chat;



import com.example.polls.model.chat.ChatMessage;
import com.example.polls.model.chat.MessageStatusName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

//    long countBySenderIdAndRecipientIdAndStatus(
//            Long senderId, Long recipientId, MessageStatusName status);

    List<ChatMessage> findByChat_Id(Long chatId);
    Optional<ChatMessage> findById(Long id);
}
