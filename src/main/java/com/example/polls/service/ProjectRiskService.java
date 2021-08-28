package com.example.polls.service;

import com.example.polls.model.project.*;
import com.example.polls.payload.requests.project.ProjectRiskDto;
import com.example.polls.repository.project.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProjectRiskService {

    private final ProjectRiskRepository repository;
    private final RiskDBRepository riskDBRepository;


    @Transactional
    public ProjectRisk create(ProjectRisk risk) {
        return repository.save(risk);
    }

    public ProjectRisk get(Long riskId) {
        return repository.findById(riskId).get();
    }

}
