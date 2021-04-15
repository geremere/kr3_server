package com.example.polls.payload.requests.project;

import com.example.polls.model.project.RiskState;
import com.example.polls.model.user.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskRequest {
    private Long id;
    private List<Long> owners;
    private Long project_id;
    private Long risk_id;
    private String state;
    private String title;
    private String description;
    private Long priority;
    private Long state_id;
    private Long changerId;
}
