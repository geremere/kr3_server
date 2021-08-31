package com.example.polls.payload.response;

import com.example.polls.model.Amazon.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ChatRoomResponse {
    private Long id;
    private String title;
    private List<Long> usersId;
    private Image image;
}
