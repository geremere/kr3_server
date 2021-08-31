package com.example.polls.model.chat;

import com.example.polls.model.audit.DateAudit;
import com.example.polls.model.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "chatmessage")
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @JoinColumn(name = "content")
    private String content;

    @Column(name = "is_delivered")
    private boolean isDelivered;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "date_updated")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
