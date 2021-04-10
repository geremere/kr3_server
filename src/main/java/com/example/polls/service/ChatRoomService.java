package com.example.polls.service;

import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.chat.ChatRoomType;
import com.example.polls.model.chat.ChatRoomTypeEnum;
import com.example.polls.repository.chat.ChatRoomRepository;
import com.example.polls.repository.chat.ChatRoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatRoomTypeRepository chatRoomTypeRepository;


    public ChatRoom getChatRoom(Long id, String type) {
        if (id == null) {
            ChatRoomType chatRoomType = chatRoomTypeRepository.findChatRoomTypeByType(ChatRoomTypeEnum.valueOf(type)).get();
            ChatRoom chatRoom = ChatRoom
                    .builder()
                    .title("")
                    .type(chatRoomType)
                    .build();
            chatRoomRepository.save(chatRoom);
            return chatRoom;
        }
        return chatRoomRepository.findById(id).get();
    }
}
