package com.example.polls.repository.chat;



import com.example.polls.model.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    Optional<ChatMessage> findById(Long id);
}
