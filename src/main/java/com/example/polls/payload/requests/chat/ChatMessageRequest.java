package com.example.polls.payload.requests.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessageRequest {

    private Long chatId;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String content;
    private String type;
    private MessageStatusRequest status;
}
