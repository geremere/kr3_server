package com.example.polls.payload.chat;

import com.example.polls.payload.UserSummary;
import lombok.*;

@Getter
@Setter
@Builder
public class ChatNotification {

    private UserSummary sender;
    private Long chatId;
}