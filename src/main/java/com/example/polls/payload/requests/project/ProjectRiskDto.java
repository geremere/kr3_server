package com.example.polls.payload.requests.project;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectRiskDto{

    private Long id;
    private Double cost;
    private Double probability;
    private Boolean is_outer;
    private RiskDto risk;

}
