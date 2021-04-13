package com.example.polls.repository.project;

import com.example.polls.model.project.RiskState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskStateRepository  extends JpaRepository<RiskState,String> {
    RiskState findById(Long id);
}
