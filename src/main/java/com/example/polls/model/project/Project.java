package com.example.polls.model.project;

import com.example.polls.model.Amazon.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String Description;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<Risk> risks = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<Comment> feed = new ArrayList<>();

    public void addRisk(Risk risk) {
        risks.add(risk);
        risk.setProject(this);
    }

    public void removeRisk(Risk risk) {
        risks.remove(risk);
    }

    public void addComment(Comment comment) {
        feed.add(comment);
        comment.setProject(this);
    }

    public String toString() {
        return "Project: " + getId();
    }
}
