package com.example.polls.payload.chat;

import com.example.polls.payload.UserSummary;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private UserSummary sender;
    private String content;
    private String updatedAt;
}
