package com.example.polls.service;

import com.example.polls.model.user.RegTypeName;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getById(Long id) {
        return userRepository.findById(id).get();
    }

    public boolean isVkUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.get().getRegTypeNames().contains(RegTypeName.VK);
    }

    public User update(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    public List<User> search(String search, Long id) {
        return userRepository.findByNameContaining(search).stream()
                .filter(user -> !user.getId().equals(id))
                .collect(Collectors.toList());

    }

    public UserSummary toSummary(User user) {
        return UserSummary.builder()
                .image(user.getImage())
                .name(user.getName())
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
