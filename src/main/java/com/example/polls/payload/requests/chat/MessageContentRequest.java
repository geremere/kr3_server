package com.example.polls.payload.requests.chat;

import com.example.polls.model.chat.MessageContentType;
import com.example.polls.model.chat.MessageStatusName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MessageContentRequest {
    private Long id;
    private MessageContentType type;
}
