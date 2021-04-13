package com.example.polls.payload.requests.project;

import com.example.polls.model.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectRequest {

    private List<User> users = new ArrayList<>();
    private String title;
    private String description;
    private Long image_id;
}
