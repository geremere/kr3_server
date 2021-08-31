package com.example.polls.repository.chat;


import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    ChatRoom findById(Long Id);

    @Query(value = "select exists " +
            "(select 1 from chatroom_users cu " +
            "join chatroom_users c on cu.chatroom_id = c.chatroom_id " +
            "join chatroom cr on cr.id = cu.chatroom_id " +
            "where c.user_id = :senderId and cu.user_id = :recipientId and cr.is_dialog = false)",
            nativeQuery = true)
    boolean isChatExist(@Param("senderId") Long senderId,
                        @Param("recipientId") Long recipientId);

    @Query(value = "select cr.id, cr.is_dialog, cr.image_id, cr.title from chatroom_users cu " +
            "join chatroom_users c on cu.chatroom_id = c.chatroom_id " +
            "join chatroom cr on cr.id = cu.chatroom_id " +
            "where c.user_id = 1 and cu.user_id = 2 and cr.is_dialog = false",
            nativeQuery = true)
    ChatRoom findChatByUsersId(@Param("senderId") Long senderId,
                               @Param("recipientId") Long recipientId);

}