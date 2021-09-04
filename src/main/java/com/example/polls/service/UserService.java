package com.example.polls.service;

import com.example.polls.exception.AppException;
import com.example.polls.model.user.RegType;
import com.example.polls.model.user.RegTypeName;
import com.example.polls.model.user.User;
import com.example.polls.payload.UserSummary;
import com.example.polls.payload.user.ChangePasswordDto;
import com.example.polls.repository.RegTypeRepository;
import com.example.polls.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegTypeRepository regTypeRepository;

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new AppException("Not Found User"));
    }

    public boolean isVkUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new AppException("Not Found User"));
        return user.getRegTypeNames().contains(RegTypeName.VK);
    }

    public User update(Long id, UserSummary user) {
        User oldUser = getById(id);
        oldUser.setEmail(user.getEmail());
        oldUser.setName(user.getName());
        oldUser.setUsername(user.getUsername());
        return save(oldUser);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public List<User> search(String search, Long id) {
        return userRepository.findByNameContaining(search).stream()
                .filter(user -> !user.getId().equals(id))
                .collect(Collectors.toList());

    }

    public boolean hasDefaultRegistration(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found"))
                .getRegTypeNames()
                .contains(RegTypeName.DEFAULT);
    }

    public boolean changePassword(ChangePasswordDto changePasswordDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found"));
        if (user.getRegTypeNames().contains(RegTypeName.DEFAULT)) {
            if (passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } else {
            Set<RegType> regTypes = user.getRegTypes();
            regTypes.add(regTypeRepository.findByName(RegTypeName.DEFAULT));
            user.setRegTypes(regTypes);
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            return true;
        }
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
