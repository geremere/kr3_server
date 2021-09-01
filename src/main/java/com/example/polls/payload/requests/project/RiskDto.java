package com.example.polls.payload.requests.project;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskDto {
    private Long id;
    private String name;
    private String description;
    private String type;
}
