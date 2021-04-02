package com.example.polls.model.chat;

import com.example.polls.model.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "chatmessage")
public class ChatMessage{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="chat_id")
    private String chatId;

    @Column(name="sender_id")
    private Long senderId;

    @Column(name="recipient_id")
    private Long recipientId;

    @Column(name="sender_name")
    private String senderName;

    @Column(name="recipient_name")
    private String recipientName;

    @Column(name="content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private MessageStatus status;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "status_id", referencedColumnName = "id", insertable=false, updatable=false)
//    private MessageStatus status;
}
