package com.example.polls.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "messagestatus")
public class MessageStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    @Getter
    @Setter
    private MessageStatusName name;

    @OneToMany(mappedBy = "status")
    @JsonIgnore
    @Getter
    @Setter
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
        message.setStatus(this);
    }
    public void removeMessage(ChatMessage message) {
        chatMessages.remove(message);
        message.setStatus(null);
    }
     public String toString(){
        return id+": "+name;
     }
}
