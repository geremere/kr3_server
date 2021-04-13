package com.example.polls.service;

import com.example.polls.model.user.RegTypeName;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public Set<RegType> getUserRegType(Long id) {
//        Optional<User> user = userRepository.findById(id);
//        return user.get().getRegType();
//    }

    public boolean isVkUser(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.get().getRegTypeNames().contains(RegTypeName.VK);
    }

    public UserSummary getSummary(User user){
        return UserSummary.builder()
                .image(user.getImage())
                .name(user.getName())
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
