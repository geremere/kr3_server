package com.example.polls.payload.chat;

import com.example.polls.payload.UserSummary;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MessageDto {
    private Long id;
    private UserSummary sender;
    private String content;
    private String updatedAt;
}
