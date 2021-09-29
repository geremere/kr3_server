package com.example.polls.controller;

import com.example.polls.exception.AppException;
import com.example.polls.model.Amazon.AWSFile;
import com.example.polls.model.project.ProjectRisk;
import com.example.polls.model.project.Risk;
import com.example.polls.payload.requests.project.ProjectRequest;
import com.example.polls.payload.response.project.ProjectResponse;
import com.example.polls.payload.response.project.ProjectRiskSensitivityDto;
import com.example.polls.repository.project.RiskDBRepository;
import com.example.polls.security.CurrentUser;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.AWSFileService;
import com.example.polls.service.ProjectRiskService;
import com.example.polls.service.ProjectService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProjectController {
    private final ProjectService projectService;
    private final RiskDBRepository riskDBRepository;
    private final AWSFileService fileService;
    private final ProjectRiskService projectRiskService;

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

    @GetMapping("/project/search/{search}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ProjectResponse>> search(@PathVariable(name = "search", required = false) String title) {
        return ResponseEntity.ok(projectService.search(title).stream()
                .map(projectService::getResponse)
                .collect(Collectors.toList()));
    }


    @GetMapping("/project/{prId}")
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

    @PostMapping("/risks/file/{riskId}")
    @PreAuthorize("hasRole('USER')")
    public Map<String, List<Double>> saveExcel(@RequestParam(value = "file", required = true) MultipartFile file,
                                               @PathVariable(name = "riskId", required = true) Long riskId) {
        try {
            AWSFile savedFile = fileService.store(file);
            ProjectRisk risk = projectRiskService.get(riskId);
            risk.setExcel(savedFile);
            projectRiskService.update(risk, riskId);
            return projectRiskService.getTable(riskId);
        } catch (IOException ex) {
            throw new AppException("error in file saved");
        }
    }

    @GetMapping("/risks/file/{riskId}")
    @PreAuthorize("hasRole('USER')")
    public Map<String, List<Double>> getExcel(@PathVariable(name = "riskId", required = true) Long riskId) {
        return projectRiskService.getTable(riskId);
    }

    @DeleteMapping("/project/{id}")
    @PreAuthorize("hasRole('USER')")
    public void delete(@PathVariable(name = "id", required = true) Long id) {
        projectService.delete(id);
    }

    @GetMapping("/project/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ProjectResponse>> getListByOwner(@PathVariable(name = "userId", required = true) Long userId) {
        return ResponseEntity.ok(projectService.listByUser(userId));
    }

    @GetMapping("/project/closed/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ProjectResponse>> getListByOwnerClosed(@PathVariable(name = "userId", required = true) Long userId) {
        return ResponseEntity.ok(projectService.listByUserClosed(userId));
    }

}
