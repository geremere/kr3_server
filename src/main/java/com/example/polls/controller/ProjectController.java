package com.example.polls.controller;

import com.example.polls.model.project.Comment;
import com.example.polls.model.project.Project;
import com.example.polls.model.project.Risk;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.payload.requests.project.ProjectRequest;
import com.example.polls.payload.response.project.ProjectResponse;
import com.example.polls.payload.response.project.RiskResponse;
import com.example.polls.repository.FileRepository;
import com.example.polls.repository.project.ProjectRepository;
import com.example.polls.service.ProjectService;
import com.example.polls.service.RiskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final FileRepository fileRepository;
    private final RiskService riskService;
    private final ProjectService projectService;

    @PostMapping("project/create")
    public ResponseEntity<ProjectResponse> createProject(ProjectRequest projectRequest) {
        Project saved = projectRepository.save(Project.builder()
                .users(projectRequest.getUsers())
                .image(projectRequest.getImage_id() != null ? fileRepository.findByImageId(projectRequest.getImage_id()) : null)
                .title(projectRequest.getTitle())
                .description(projectRequest.getDescription())
                .build());
        List<UserSummary> users = projectRequest.getUsers().stream().map(user -> UserSummary.builder()
                .name(user.getName())
                .username(user.getUsername())
                .id(user.getId())
                .image(user.getImage())
                .build()).collect(Collectors.toList());

        return ResponseEntity.ok(ProjectResponse.builder()
                .description(saved.getDescription())
                .users(users)
                .image_url(saved.getImage().getUrl())
                .title(saved.getTitle())
                .id(saved.getId())
                .build());
    }

    @PostMapping("project/risks/get")
    public ResponseEntity<List<RiskResponse>> getRisks(Long prId) {
        Project project = projectRepository.findById(prId).get();
        return ResponseEntity.ok(project.getRisks().stream()
                .map(risk -> riskService.getRisk(risk.getId()))
                .collect(Collectors.toList()));

    }

    @PostMapping("project/{prId}/add/user/{userId}")
    public void getRisks(@PathVariable Long prId,
                         @PathVariable Long userId) {
        projectService.addUser(prId, userId);
    }

    //    @GetMapping("/project/users/{prId}/{riskId}")
    //    public ResponseEntity<List<UserSummary>> getRisk(@PathVariable Long prId,
    //                                                     @PathVariable Long riskId) {
    //        return ResponseEntity
    //                .ok(riskService.getRisk(riskId));
    //    }
}
