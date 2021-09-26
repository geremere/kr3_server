package com.example.polls.service;

import com.example.polls.model.Amazon.Image;
import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.project.Project;
import com.example.polls.model.project.ProjectRisk;
import com.example.polls.model.project.Risk;
import com.example.polls.model.project.RiskTypeEnum;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.payload.requests.project.ProjectRequest;
import com.example.polls.payload.requests.project.ProjectRiskDto;
import com.example.polls.payload.requests.project.RiskDto;
import com.example.polls.payload.response.project.ProjectResponse;
import com.example.polls.repository.chat.ChatRoomRepository;
import com.example.polls.repository.project.ProjectRepository;
import com.example.polls.repository.project.RiskDBRepository;
import com.example.polls.repository.project.RiskTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final RiskDBRepository riskDBRepository;
    private final AWSImageService imageService;
    private final RiskTypeRepository riskTypeRepository;
    private static Random rnd = new Random();


    @Transactional
    public ProjectResponse create(ProjectRequest projectRequest) throws IOException {
        List<User> users = projectRequest.getUsers().stream().map(user -> userService.getById(user.getId())).collect(Collectors.toList());
        User owner = userService.getById(projectRequest.getOwner_id());
        Image image = imageService.getFile(projectRequest.getImage_id());
        Project project = projectRepository.save(Project.builder()
                .image(image)
                .title(projectRequest.getTitle())
                .description(projectRequest.getDescription())
                .users(users)
                .owner(owner)
                .build());
//        owner.addProject(project);
        users.add(owner);
        chatRoomRepository.save(ChatRoom.builder()
                .isDialog(true)
                .image(project.getImage())
                .title(project.getTitle())
                .users(users)
                .build());
//        userService.save(owner);
        return getResponse(project);
    }

    public Project get(Long id) {
        Project project = projectRepository.findById(id).get();
        return project;
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectRequest projectRequest) {
        Project oldProject = get(id);
        List<User> users = projectRequest.getUsers().stream().map(user -> userService.getById(user.getId())).collect(Collectors.toList());
        List<ProjectRisk> risks = projectRequest.getRisks().stream().map((risk) -> {
            Risk newRisk = Risk.builder()
                    .id(risk.getRisk().getId())
                    .name(risk.getRisk().getName())
                    .description(risk.getRisk().getDescription())
                    .type(riskTypeRepository.findByType(RiskTypeEnum.of(risk.getRisk().getType())))
                    .build();
            Risk riskdb = riskDBRepository.save(newRisk);
            ProjectRisk newProjectRisk = ProjectRisk.builder()
                    .id(risk.getId())
                    .risk(riskDBRepository.findById(riskdb.getId()))
                    .cost(risk.getCost())
                    .is_outer(risk.getIs_outer())
                    .probability(risk.getProbability())
                    .isSolved(risk.getIs_solved() != null && risk.getIs_solved())
                    .build();
            return newProjectRisk;
        }).collect(Collectors.toList());
        User owner = userService.getById(projectRequest.getOwner_id());
        Image image = imageService.getFile(projectRequest.getImage_id());
        Project project = projectRepository.save(Project.builder()
                .id(oldProject.getId())
                .image(image)
                .title(projectRequest.getTitle())
                .description(projectRequest.getDescription())
                .users(users)
                .owner(owner)
                .risks(risks)
                .build());
        owner.addProject(project);
        userService.save(owner);
        return getResponse(project);
    }

    public ProjectResponse getResponse(Project project) {
        List<UserSummary> users = project.getUsers().stream()
                .filter((user) -> !user.getId().equals(project.getOwner().getId()))
                .map(user -> UserSummary.builder()
                        .name(user.getName())
                        .username(user.getUsername())
                        .id(user.getId())
                        .image(user.getImage())
                        .build()).collect(Collectors.toList());
        List<ProjectRiskDto> risks = project.getRisks() != null ?
                project.getRisks().stream()
                        .map(risk -> ProjectRiskDto.builder()
                                .id(risk.getId())
                                .cost(risk.getCost())
                                .probability(risk.getProbability())
                                .is_outer(risk.getIs_outer())
                                .hasSensitivity(risk.getExcel() != null)
                                .risk(RiskDto.builder()
                                        .id(risk.getRisk().getId())
                                        .name(risk.getRisk().getName())
                                        .description(risk.getRisk().getDescription())
                                        .type(risk.getRisk().getType().getType().getValue())
                                        .build())
                                .is_solved(risk.getIsSolved())
                                .build())
                        .collect(Collectors.toList())
                : new ArrayList<>();
        return ProjectResponse.builder()
                .description(project.getDescription())
                .users(users)
                .image_url(project.getImage() != null ? project.getImage().getUrl() : null)
                .title(project.getTitle())
                .id(project.getId())
                .owner_id(project.getOwner().getId())
                .risks(risks)
                .build();
    }

    public List<ProjectResponse> list() {
        return projectRepository.findAll().stream()
                .filter(project -> !project.isDeleted())
                .map(this::getResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id){
        Project project = get(id);
        project.setDeleted(true);
        projectRepository.save(project);
    }

    public List<ProjectResponse> listByUser(Long id){
        User user = userService.getById(id);
        return projectRepository.findAllByOwner(user).stream()
                .filter(project -> !project.isDeleted())
                .map(this::getResponse)
                .collect(Collectors.toList());
    }

    public List<ProjectResponse> listByUserClosed(Long id){
        User user = userService.getById(id);
        return projectRepository.findAllByOwner(user).stream()
                .filter(Project::isDeleted)
                .map(this::getResponse)
                .collect(Collectors.toList());
    }


}
