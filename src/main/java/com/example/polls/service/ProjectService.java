package com.example.polls.service;

import com.example.polls.model.Amazon.Image;
import com.example.polls.model.project.Project;
import com.example.polls.model.project.ProjectRisk;
import com.example.polls.model.project.Risk;
import com.example.polls.model.project.RiskType;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.payload.requests.project.ProjectRequest;
import com.example.polls.payload.response.project.ProjectResponse;
import com.example.polls.repository.FileRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.project.ProjectRepository;
import com.example.polls.repository.project.ProjectRiskRepository;
import com.example.polls.repository.project.RiskDBRepository;
import com.example.polls.repository.project.RiskTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RiskDBRepository riskDBRepository;
    private final AWSImageService imageService;


    @Transactional
    public ProjectResponse createProject(ProjectRequest projectRequest) throws IOException {
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
        return getResponse(project);
    }

    @Transactional
    public List<Long> getUsersIdByProject(Long prId) {
        return projectRepository.findById(prId).get().
                getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public List<UserSummary> getAvailableUser(Long prId, Long riskId) {
        Risk projectRisk = riskDBRepository.findById(riskId);
        Project project = projectRepository.findById(prId).get();
        List<User> users = project.getUsers();
        List<UserSummary> res = new ArrayList<>();
        for (RiskType type : projectRisk.getTypes()) {
            for (User user : users) {
                List<Long> usersSpecialityesId = user.getSpeciality().stream().map(RiskType::getId).collect(Collectors.toList());
                List<Long> choosenUsers = res.stream().map(UserSummary::getId).collect(Collectors.toList());
                if (usersSpecialityesId.contains(type.getId()) && !choosenUsers.contains(user.getId())) {
                    res.add(userService.getSummary(user));
                }

            }
        }
        return res;
    }

    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id).get();
        return getResponse(project);
    }

    public void addUser(Long prId, Long userId) {
        User user = userRepository.findById(userId).get();
        Project project = projectRepository.findById(prId).get();
        project.getUsers().add(user);
        projectRepository.save(project);
    }

    public ProjectResponse getResponse(Project project) {
        List<UserSummary> users = project.getUsers().stream().map(user -> UserSummary.builder()
                .name(user.getName())
                .username(user.getUsername())
                .id(user.getId())
                .image(user.getImage())
                .build()).collect(Collectors.toList());
        return ProjectResponse.builder()
                .description(project.getDescription())
                .users(users)
                .image_url(project.getImage() != null ? project.getImage().getUrl() : null)
                .title(project.getTitle())
                .id(project.getId())
                .build();
    }

    public List<ProjectResponse> getAllProjects(Long id) {
        User user = userService.getById(id);
        List<ProjectResponse> lst = projectRepository.findAllByOwner(user)
                .stream().map(this::getResponse)
                .collect(Collectors.toList());
        return projectRepository.findAllByOwner(user)
                .stream().map(this::getResponse)
                .collect(Collectors.toList());
    }


}
