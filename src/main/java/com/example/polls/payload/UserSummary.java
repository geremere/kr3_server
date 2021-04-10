package com.example.polls.payload;

import com.example.polls.model.Amazon.Image;
import lombok.Getter;
import lombok.Setter;

public class UserSummary {
    private Long id;
    private String username;
    private String name;
    @Getter
    @Setter
    private Image image;

    public UserSummary(Long id, String username, String name, Image image) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
