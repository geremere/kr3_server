package com.example.polls.model.project;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "risktypes")
public class RiskType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RiskTypeEnum type;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "risks_users",
//            joinColumns = @JoinColumn(name = "chatroom_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//    @JsonIgnore
//    private List<User> users = new ArrayList<>();
}
