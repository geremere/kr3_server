package com.example.polls.payload.requests.chat;

import com.example.polls.model.chat.MessageStatusName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Builder
public class MessageStatusRequest {

    private Long id;
    private MessageStatusName name;
}
