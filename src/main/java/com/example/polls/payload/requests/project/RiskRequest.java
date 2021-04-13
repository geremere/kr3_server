package com.example.polls.payload.requests.project;

import com.example.polls.model.project.RiskState;
import com.example.polls.model.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class RiskRequest {
    private Long id;
    private List<Long> owners;
    private Long project_id;
    private List<String> riskType;
    private RiskState state;
    private Long changerId;
}
