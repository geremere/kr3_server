package com.example.polls.payload.requests.project;

import com.example.polls.model.project.RiskStateEnum;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RiskStateRequest {
    private Long id;

    private String title;

    private Long priority;

    private String description;
    private String state;

}
