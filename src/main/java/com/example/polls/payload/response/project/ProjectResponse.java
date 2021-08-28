package com.example.polls.payload.response.project;

import com.example.polls.model.project.ProjectRisk;
import com.example.polls.payload.UserSummary;
import com.example.polls.payload.requests.project.ProjectRiskDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectResponse {
    private Long id;

    private String title;

    private String image_url;

    private String description;

    private Long owner_id;

    private List<UserSummary> users;

    private List<ProjectRiskDto> risks;
}
