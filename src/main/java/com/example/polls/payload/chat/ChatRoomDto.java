package com.example.polls.payload.chat;

import com.example.polls.model.Amazon.Image;
import com.example.polls.payload.UserSummary;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatRoomDto {
    private Long id;
    private List<UserSummary> users;
    private String title;
    private List<MessageDto> messages;
    private String lastMessage;
    private Image image;
    private Boolean isDialog;
}
