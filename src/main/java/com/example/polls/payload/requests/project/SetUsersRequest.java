package com.example.polls.payload.requests.project;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SetUsersRequest {
    private Long projectId;
    private List<Long> users;
}
