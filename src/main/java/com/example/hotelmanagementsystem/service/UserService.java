package com.example.hotelmanagementsystem.service;

import com.example.hotelmanagementsystem.entity.User;
import com.example.hotelmanagementsystem.exception.UserNotFoundException;
import com.example.hotelmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public Page<User> getAllUsers(int page, int size) {
        log.info("UserService::getAllUsers - Fetching page {} with size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    public User getUserById(Long id) {
        log.info("UserService::getUserById - Fetching user with id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        log.info("UserService::getUserByEmail - Fetching user with email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public void deleteUser(Long id) {
        log.info("UserService::deleteUser - Deleting user with id: {}", id);
        User user = getUserById(id);
        userRepository.delete(user);
        log.info("UserService::deleteUser - User deleted successfully: {}", id);
    }
}

