package com.example.polls.model.chat;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
public class ChatNotification {

    private Long Id;
    private Long senderId;
    private String senderName;
    private Long chatId;
}