package com.example.polls.service;

import com.example.polls.model.chat.ChatMessage;
import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.chat.ChatRoomType;
import com.example.polls.model.chat.ChatRoomTypeEnum;
import com.example.polls.model.user.User;
import com.example.polls.payload.requests.chat.ChatMessageRequest;
import com.example.polls.repository.UserRepository;
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
    @Autowired
    private  UserRepository userRepository;



    public ChatRoom getChatRoom(ChatMessageRequest chatMessage) {
//        код будет работать тут только для диалогов, чаты для более чем одного будут создаваться отдельно
        if (chatMessage.getChatId() == null) {
            return createChatRoom(chatMessage.getRecipientsId(),chatMessage.getSenderId(),"DIALOG");
        }
        return chatRoomRepository.findById(chatMessage.getChatId()).get();
    }

    public ChatRoom createChatRoom(Long[] recipientsId, Long senderId, String type){
        ChatRoomType chatRoomType = chatRoomTypeRepository.findChatRoomTypeByType(ChatRoomTypeEnum.valueOf(type)).get();
        ChatRoom chatRoom = ChatRoom
                .builder()
                .title("")
                .type(chatRoomType)
                .build();
        chatRoomRepository.save(chatRoom);

        User user = userRepository.findById(recipientsId[0]).get();
        user.getChatRooms().add(chatRoom);
        if(!recipientsId[0].equals(senderId)) {
            User currentUser = userRepository.findById(senderId).get();
            currentUser.getChatRooms().add(chatRoom);
        }
        return chatRoom;
    }
}
