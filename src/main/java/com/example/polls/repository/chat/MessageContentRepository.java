package com.example.polls.repository.chat;

import com.example.polls.model.chat.MessageContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageContentRepository  extends JpaRepository<MessageContent,Long> {
}
