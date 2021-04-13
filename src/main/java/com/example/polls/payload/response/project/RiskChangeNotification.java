package com.example.polls.payload.response.project;

import com.example.polls.model.project.RiskState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RiskChangeNotification {
    private Long Id;
    private String changerName;
}
