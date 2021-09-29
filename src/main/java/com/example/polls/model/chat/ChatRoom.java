package com.example.polls.model.chat;

import com.example.polls.model.Amazon.Image;
import com.example.polls.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chatroom")
@Getter
@Setter
public class ChatRoom{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "is_dialog")
    private Boolean isDialog;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chat_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chatroom_users",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users = new ArrayList<>();
}
