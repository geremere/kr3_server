package com.example.polls.repository.project;

import com.example.polls.model.project.ProjectRisk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRiskRepository extends JpaRepository<ProjectRisk,String> {
    Optional<ProjectRisk> findById(Long id);
}
