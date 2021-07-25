package com.example.polls.repository.project;

import com.example.polls.model.project.Project;
import com.example.polls.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ProjectRepository extends JpaRepository<Project, String> {
    Optional<Project> findById(Long id);
    List<Project> findAllByOwner(User owner);
    List<Project> findAll();

}
