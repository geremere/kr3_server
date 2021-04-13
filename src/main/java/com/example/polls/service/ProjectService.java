package com.example.polls.service;

import com.example.polls.model.project.Project;
import com.example.polls.model.project.Risk;
import com.example.polls.model.project.RiskType;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.project.ProjectRepository;
import com.example.polls.repository.project.RiskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final RiskRepository riskRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<Long> getUsersIdByProject(Long prId) {
        return projectRepository.findById(prId).get().
                getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public List<UserSummary> getAvailableUser(Long prId, Long riskId) {
        Risk risk = riskRepository.findById(riskId).get();
        Project project = projectRepository.findById(prId).get();
        List<UserSummary> users = new ArrayList<>();
        for (User it : project.getUsers()) {
            for (RiskType type : risk.getTypes()) {
                if (it.getSpeciality().stream().anyMatch(uType -> uType.getId().equals(type.getId())))
                    users.add(userService.getSummary(it));
                break;
            }
        }
        return users;
    }

    public void addUser(Long prId, Long userId){
        User user = userRepository.findById(userId).get();
        Project project = projectRepository.findById(prId).get();
        project.getUsers().add(user);
        projectRepository.save(project);
    }
}
