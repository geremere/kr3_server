package com.example.polls.payload.requests.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomCreateRequest {
    private Long[] recipientsId;
    private Long senderId;
    private String type;
}
