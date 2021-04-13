package com.example.polls.repository.project;

import com.example.polls.model.project.Risk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiskRepository extends JpaRepository<Risk,String> {
    Optional<Risk> findById(Long id);
}
