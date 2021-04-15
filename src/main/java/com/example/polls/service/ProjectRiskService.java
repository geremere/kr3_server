package com.example.polls.service;

import com.example.polls.model.project.*;
import com.example.polls.model.user.User;
import com.example.polls.payload.requests.project.RiskRequest;
import com.example.polls.payload.response.project.RiskResponse;
import com.example.polls.payload.response.project.RiskTypeResponse;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.project.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectRiskService {
    private final ProjectRepository projectRepository;
    private final RiskStateRepository riskStateRepository;
    private final RiskTypeRepository riskTypeRepository;
    private final ProjectRiskRepository projectRiskRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RiskDBRepository riskDBRepository;

    @Transactional
    public ProjectRisk save(RiskRequest riskRequest) {

        if(riskRequest.getId()==null)
        {
            List<User> users = new ArrayList<>();
            for (Long it : riskRequest.getOwners()) {
                users.add(userRepository.findById(it).get());
            }
            Risk risk = riskDBRepository.findById(riskRequest.getRisk_id());

            Project project = projectRepository.findById(riskRequest.getProject_id()).get();
            RiskState riskState = RiskState.builder()
                    .description(riskRequest.getDescription())
                    .priority(riskRequest.getPriority())
                    .state(riskRequest.getState())
                    .title(riskRequest.getTitle())
                    .build();
            riskStateRepository.save(riskState);
            ProjectRisk projectRiskEntity = ProjectRisk.builder()
                    .id(riskRequest.getId())
                    .risk(risk)
                    .owners(users)
                    .state(riskState)
                    .project(project)
                    .build();


            return projectRiskRepository.save(projectRiskEntity);
        }else{
            RiskState riskState = riskStateRepository.findById(riskRequest.getState_id());
            riskState.setPriority(riskRequest.getPriority());
            riskState.setDescription(riskRequest.getDescription());
            riskState.setTitle(riskRequest.getTitle());
            riskState.setState(riskRequest.getState());
            riskStateRepository.save(riskState);
            ProjectRisk projectRisk = projectRiskRepository.findById(riskRequest.getId()).get();
            return projectRisk;
        }
    }

    public RiskResponse getRisk(Long riskId){
        ProjectRisk projectRisk = projectRiskRepository.findById(riskId).get();
        return RiskResponse.builder()
                .id(projectRisk.getId())
                .owners(projectRisk.getOwners().stream().map(userService::getSummary).collect(Collectors.toList()))
                .risk(projectRisk.getRisk())
                .state(projectRisk.getState()).build();
    }

    public List<RiskTypeResponse> getAllTypes(){
        return riskTypeRepository.findAll().stream().map(type->RiskTypeResponse.builder()
                .id(type.getId())
                .type(type.getType().toString())
        .build()).collect(Collectors.toList());
    }

    public List<Risk> getRisksByTypeId(Long id){
        List<Risk> risks = riskDBRepository.findAll();
        List<Risk> res = new ArrayList<>();
        for(Risk risk:risks){
            List<Long> risktypesId = risk.getTypes().stream().map(RiskType::getId).collect(Collectors.toList());
            if(risktypesId.indexOf(id)!=-1)
                res.add(risk);
        }
        return res;
    }

}
