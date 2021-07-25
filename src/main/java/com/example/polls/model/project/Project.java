package com.example.polls.model.project;

import com.example.polls.model.Amazon.Image;
import com.example.polls.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    private String description;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<ProjectRisk> projectRisks = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<Comment> feed = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "projects_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public void addRisk(ProjectRisk projectRisk) {
        projectRisks.add(projectRisk);
        projectRisk.setProject(this);
    }

    public void removeRisk(ProjectRisk projectRisk) {
        projectRisks.remove(projectRisk);
    }

    public void addComment(Comment comment) {
        feed.add(comment);
        comment.setProject(this);
    }

    public String toString() {
        return "Project: " + getId();
    }
}
