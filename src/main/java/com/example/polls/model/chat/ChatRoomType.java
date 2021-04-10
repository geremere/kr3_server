package com.example.polls.model.chat;

import com.example.polls.model.user.RegTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "chatroomtypes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private ChatRoomTypeEnum type;
}
