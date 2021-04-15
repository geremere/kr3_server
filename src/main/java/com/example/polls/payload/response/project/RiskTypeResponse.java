package com.example.polls.payload.response.project;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RiskTypeResponse {
    private Long id;
    private String type;
}
