package com.example.polls.model.project;

import com.example.polls.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "riskstates")
public class RiskState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long priority = new Long(1);

    private String description = "";


    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RiskStateEnum state = RiskStateEnum.BEGIN;

//    @OneToOne(fetch = FetchType.EAGER)
//    @Column(name = "risk_id")
//    private Risk risk;
}
