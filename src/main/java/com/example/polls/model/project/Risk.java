package com.example.polls.model.project;

import com.example.polls.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "risks")
public class Risk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "risks_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "risk_id"))
    @JsonIgnore
    private List<User> owners = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Project project;

    @ManyToMany
    @JoinTable(name = "risktypes_risks",
            joinColumns = @JoinColumn(name = "risk_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    @JsonIgnore
    private List<RiskType> types = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "state_id")
    private RiskState state;

    public void addUser(User user){
        owners.add(user);
        user.getResponsibility().add(this);
    }

    public void removeUser(User user){
        owners.remove(user);
        user.getResponsibility().remove(this);
    }

    public String toString() {
        return "ProjectRisk: " + getId();
    }
}
