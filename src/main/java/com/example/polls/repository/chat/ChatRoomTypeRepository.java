package com.example.polls.repository.chat;

import com.example.polls.model.chat.ChatRoomType;
import com.example.polls.model.chat.ChatRoomTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomTypeRepository extends JpaRepository<ChatRoomType,Long> {
    Optional<ChatRoomType> findChatRoomTypeByType(ChatRoomTypeEnum type);
}
