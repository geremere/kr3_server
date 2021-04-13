package com.example.polls.payload.response.project;

import com.example.polls.model.project.Comment;
import com.example.polls.model.project.Risk;
import com.example.polls.payload.UserSummary;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class ProjectResponse {
    private Long id;

    private String title;

    private String image_url;

    private String description;

    private List<UserSummary> users;
}
