package com.example.polls.repository.project;

import com.example.polls.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {
    Optional<Project> findById(Long id);
    List<Project> findAll();
}