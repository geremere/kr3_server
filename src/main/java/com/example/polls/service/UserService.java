package com.example.polls.service;

import com.example.polls.model.project.Project;
import com.example.polls.model.user.RegTypeName;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.repository.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> UserById(List<Long> idlist) {
        return idlist.stream().map((id) -> userRepository.findById(id).get()).collect(Collectors.toList());
    }

    public User getById(Long id) {
        return userRepository.findById(id).get();
    }

    public boolean isVkUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.get().getRegTypeNames().contains(RegTypeName.VK);
    }

    public UserSummary getSummary(User user) {
        return UserSummary.builder()
                .image(user.getImage())
                .name(user.getName())
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    public User update(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }
}
