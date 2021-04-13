package com.example.polls.repository.project;

import com.example.polls.model.project.RiskType;
import com.example.polls.model.project.RiskTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskTypeRepository  extends JpaRepository<RiskType,String> {
    RiskType findByType(RiskTypeEnum type);
}
