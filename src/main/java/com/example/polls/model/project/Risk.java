package com.example.polls.model.project;

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
@Table(name = "riskdb")
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "risk")
    @JsonIgnore
    private List<ProjectRisk> projectRisk = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "types_risks",
            joinColumns = @JoinColumn(name = "risk_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    private List<RiskType> types = new ArrayList<>();
    String name;
    String description;
}
