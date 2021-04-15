package com.example.polls.payload.requests.chat;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateRequest {
    private Long[] recipientsId;
    private Long senderId;
    private String chatName;
}
