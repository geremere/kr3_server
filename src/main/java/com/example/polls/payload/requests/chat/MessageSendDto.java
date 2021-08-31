package com.example.polls.payload.requests.chat;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendDto {
    private Long chatId;
    private Long senderId;
    private String content;
}
