package com.example.polls.repository;

import com.example.polls.model.Amazon.AWSFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<AWSFile, Long> {
    Optional<AWSFile> findById(Long id);
}

