package com.example.polls.payload.response;

import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.chat.MessageContent;
import com.example.polls.model.chat.MessageContentTypeEnum;
import com.example.polls.model.chat.MessageStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Builder
public class MessageResponse {
    private Long id;
    private Long senderId;
    private String senderName;
    private String content;
    private MessageContentTypeEnum type;
}
