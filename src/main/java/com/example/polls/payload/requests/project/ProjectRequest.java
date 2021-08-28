package com.example.polls.payload.requests.project;

import com.example.polls.model.project.ProjectRisk;
import com.example.polls.payload.UserSummary;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectRequest {

    private List<UserSummary> users;
    private List<ProjectRiskDto> risks;
    private String title;
    private String description;
    private Long image_id;
    private Long owner_id;
}

