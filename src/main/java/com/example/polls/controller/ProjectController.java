package com.example.polls.controller;

import com.example.polls.model.project.Risk;
import com.example.polls.payload.requests.project.ProjectRequest;
import com.example.polls.payload.response.project.ProjectResponse;
import com.example.polls.repository.project.RiskDBRepository;
import com.example.polls.security.CurrentUser;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProjectController {
    private final ProjectService projectService;
    private final RiskDBRepository riskDBRepository;

    @PostMapping("/project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProjectResponse> create(@RequestBody ProjectRequest projectRequest,
                                                  @CurrentUser UserPrincipal currentUser) throws IOException {
        return ResponseEntity.ok(projectService.create(projectRequest));
    }

    @GetMapping("/project")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ProjectResponse>> list() {
        return ResponseEntity.ok(projectService.list());
    }


    @GetMapping("project/{prId}")
    public ResponseEntity<ProjectResponse> get(@PathVariable Long prId) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(projectService.getResponse(projectService.get(prId)));
    }

    @PutMapping("/project/{prId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProjectResponse> update(@PathVariable Long prId,
                                                  @RequestBody ProjectRequest projectRequest) throws IOException {
        return ResponseEntity.ok(projectService.update(prId, projectRequest));
    }

    @GetMapping("/risks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Risk>> getRisks() {
        return ResponseEntity.ok(riskDBRepository.findAll());
    }
}
