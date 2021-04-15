package com.example.polls.repository.project;

import com.example.polls.model.project.Risk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskDBRepository extends JpaRepository<Risk,String> {
    Risk findById(Long id);
    List<Risk>  findAll();
}
