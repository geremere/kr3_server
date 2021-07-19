package com.example.polls.controller;

import com.example.polls.model.Amazon.Image;
import com.example.polls.model.project.Project;
import com.example.polls.model.project.Risk;
import com.example.polls.payload.UserSummary;
import com.example.polls.payload.requests.project.ProjectRequest;
import com.example.polls.payload.requests.project.SetUsersRequest;
import com.example.polls.payload.response.UploadFileResponse;
import com.example.polls.payload.response.project.ProjectResponse;
import com.example.polls.payload.response.project.RiskResponse;
import com.example.polls.payload.response.project.RiskTypeResponse;
import com.example.polls.repository.FileRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.project.ProjectRepository;
import com.example.polls.security.CurrentUser;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.AWSImageService;
import com.example.polls.service.ProjectService;
import com.example.polls.service.ProjectRiskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/api")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final ProjectRiskService projectRiskService;
    private final ProjectService projectService;
    private final AWSImageService imageService;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @PostMapping("/project/create")
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest) {
//        ProjectRequest projectRequest =  new ProjectRequest();
        return ResponseEntity.ok(projectService.createProject(projectRequest));
    }

    @GetMapping("project/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ProjectResponse>> addAllProjects(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(projectService.getAllProjects(currentUser.getId()));
    }


    @GetMapping("project/{prId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long prId) {
        return ResponseEntity.ok(projectService.getProjectById(prId));
    }

    @GetMapping("project/{prId}/get/risks")
    public ResponseEntity<List<RiskResponse>> getRisks(@PathVariable Long prId) {
        Project project = projectRepository.findById(prId).get();
        return ResponseEntity.ok(project.getProjectRisks().stream()
                .map(risk -> projectRiskService.getRisk(risk.getId()))
                .collect(Collectors.toList()));
    }

    @GetMapping("project/{prId}/add/user/{userId}")
    public void addUserToProject(@PathVariable Long prId,
                                 @PathVariable Long userId) {
        projectService.addUser(prId, userId);
    }

//    @PostMapping("/project/setimage")
//    public UploadFileResponse setUserImage(@RequestParam("file") MultipartFile file) throws IOException {
//        Image image = imageService.store(file);
//
//        fileRepository.save(image);
//
//        return UploadFileResponse.builder().id(image.getImageId()).fileName(image.getUrl()).build();
//    }

    @GetMapping("/risk/all/types")
    public ResponseEntity<List<RiskTypeResponse>> getAllTypes() {
        return ResponseEntity.ok(projectRiskService.getAllTypes());
    }

    @GetMapping("/risk/by/type/{typeId}")
    public ResponseEntity<List<Risk>> getAvailableUsers(@PathVariable Long typeId) {
        return ResponseEntity
                .ok(projectRiskService.getRisksByTypeId(typeId));
    }

    @GetMapping("/risk/users/available/{prId}/{riskId}")
    public ResponseEntity<List<UserSummary>> getAvailableUsers(@PathVariable Long prId,
                                                               @PathVariable Long riskId) {
        return ResponseEntity
                .ok(projectService.getAvailableUser(prId, riskId));
    }

    @PostMapping("/project/set/users")
    public ResponseEntity<?> setUsers(@RequestBody SetUsersRequest setUsersRequest){
        Project project = projectRepository.findById(setUsersRequest.getProjectId()).get();
        project.setUsers(userRepository.findByIdIn(setUsersRequest.getUsers()));
        projectRepository.save(project);
        return ResponseEntity.ok("success");
    }

    //    @GetMapping("/project/users/{prId}/{riskId}")
    //    public ResponseEntity<List<UserSummary>> getRisk(@PathVariable Long prId,
    //                                                     @PathVariable Long riskId) {
    //        return ResponseEntity
    //                .ok(riskService.getRisk(riskId));
    //    }
}
