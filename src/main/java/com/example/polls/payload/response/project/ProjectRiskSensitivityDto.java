package com.example.polls.payload.response.project;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ProjectRiskSensitivityDto {
    public Map<String, List<Double>> table;
}
