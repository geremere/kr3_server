package com.example.polls.payload.response.project;

import com.example.polls.model.project.RiskState;
import com.example.polls.payload.UserSummary;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RiskResponse {
    private Long id;
    private List<UserSummary> owners;
    private List<String> riskType;
    private RiskState state;
}
