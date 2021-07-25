package com.example.polls.repository;

import com.example.polls.model.chat.ChatRoom;
import com.example.polls.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findAll();

    Optional<User> findById(Long id);

//    List<User> findAll(Predicate<User> filter);

    Optional<List<User>> findByNameContaining(String substr);

    Optional<List<User>> findByNameStartingWith(String substr);
}
