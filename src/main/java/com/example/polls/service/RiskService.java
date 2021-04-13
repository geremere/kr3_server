package com.example.polls.service;

import com.example.polls.model.project.*;
import com.example.polls.model.user.User;
import com.example.polls.payload.requests.project.RiskRequest;
import com.example.polls.payload.response.project.RiskResponse;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.project.ProjectRepository;
import com.example.polls.repository.project.RiskRepository;
import com.example.polls.repository.project.RiskStateRepository;
import com.example.polls.repository.project.RiskTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RiskService {
    private final ProjectRepository projectRepository;
    private final RiskStateRepository riskStateRepository;
    private final RiskTypeRepository riskTypeRepository;
    private final RiskRepository riskRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public Risk save(RiskRequest riskRequest) {
        if (riskRequest.getState() == null || riskRequest.getState().getId() == null) {
            RiskState riskState = RiskState.builder()
                    .description(riskRequest.getState().getDescription())
                    .priority(riskRequest.getState().getPriority())
                    .build();
            riskRequest.setState(riskStateRepository.save(riskState));
        }
        List<RiskType> riskTypes = new ArrayList<>();
        for (String it : riskRequest.getRiskType()) {
            riskTypes.add(riskTypeRepository.findByType(RiskTypeEnum.valueOf(it)));
        }
        List<User> users = new ArrayList<>();
        for (Long it : riskRequest.getOwners()) {
            users.add(userRepository.findById(it).get());
        }
        Project project = projectRepository.findById(riskRequest.getProject_id()).get();
        Risk riskEntity = Risk.builder()
                .id(riskRequest.getId())
                .types(riskTypes)
                .owners(users)
                .project(project)
                .build();
        return riskRepository.save(riskEntity);

    }

    public RiskResponse getRisk(Long riskId){
        Risk risk = riskRepository.findById(riskId).get();
        return RiskResponse.builder()
                .id(risk.getId())
                .owners(risk.getOwners().stream().map(userService::getSummary).collect(Collectors.toList()))
                .riskType(risk.getTypes().stream().map(type -> type.getType().toString()).collect(Collectors.toList()))
                .state(risk.getState()).build();
    }


}
