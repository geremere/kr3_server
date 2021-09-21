package com.example.polls.model.project;

import com.example.polls.model.Amazon.AWSFile;
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
@Table(name = "project_risks")
public class ProjectRisk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="risk_id")
    private Risk risk;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name ="excel_id")
    private AWSFile excel;

    @Column
    private Boolean is_outer;

    @Column
    private Double cost;

    @Column
    private Double probability;

    @Column
    private Boolean isSolved;
}
