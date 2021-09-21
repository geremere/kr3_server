package com.example.polls.repository;

import com.example.polls.model.Amazon.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByImageId(Long imageId);

    List<Image> findAllByUrlContains(String substring);
}

