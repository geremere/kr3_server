package com.example.polls.repository.chat;

import com.example.polls.model.chat.MessageContent;
import com.example.polls.model.chat.MessageContentType;
import com.example.polls.model.chat.MessageContentTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageContentTypeRepository extends JpaRepository<MessageContentType,Long> {
    Optional<MessageContentType> findByType(MessageContentTypeEnum type);
}
