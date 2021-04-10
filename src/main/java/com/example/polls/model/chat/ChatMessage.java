package com.example.polls.model.chat;

import com.example.polls.model.audit.DateAudit;
import lombok.*;

import javax.persistence.*;
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
public class ChatMessage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private ChatRoom chat;

    @Column(name="sender_id")
    private Long senderId;

    @Column(name = "sender_name")
    private String senderName;

    @OneToOne
    @JoinColumn(name = "content_id")
    private MessageContent content;

    @ManyToOne(fetch = FetchType.EAGER)
    private MessageStatus status;

}
