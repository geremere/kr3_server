package com.example.polls.model.user;

import com.example.polls.model.Amazon.Image;
import com.example.polls.model.audit.DateAudit;
import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.project.Risk;
import com.example.polls.model.project.RiskType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "username"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        })
})
@Getter
@Setter
@NoArgsConstructor
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_regtypes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "regtype_id"))
    private Set<RegType> regTypes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "chatroom_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chatroom_id"))
    @JsonIgnore
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "risktypes_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "risktype_id"))
    @JsonIgnore
    private List<RiskType> speciality = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "risks_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "risk_id"))
    @JsonIgnore
    private List<Risk> responsibility = new ArrayList<>();


    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Set<RegTypeName> getRegTypeNames() {
        Set<RegTypeName> regTypeNameSet = new HashSet<>();
        for( RegType it:regTypes){
            regTypeNameSet.add(it.getName());
        }
        return regTypeNameSet;
    }

    public void addSpeciality(RiskType riskType){
        speciality.add(riskType);
    }

    public void removeSpeciality(RiskType riskType){
        speciality.remove(riskType);
    }
}