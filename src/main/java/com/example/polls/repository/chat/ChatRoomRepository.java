package com.example.polls.repository.chat;


import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findById(Long Id);

//    Optional<List<User>> findUsers(Long crId);

}