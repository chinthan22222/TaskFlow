package org.example.taskflow.services;

import jakarta.annotation.security.PermitAll;
import org.example.taskflow.entities.Task;
import org.example.taskflow.entities.User;
import org.example.taskflow.enums.Role;
import org.example.taskflow.exceptions.UserNotFoundException;
import org.example.taskflow.repositories.TaskRepository;
import org.example.taskflow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TaskRepository taskRepository;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }

    @PermitAll
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.DEVELOPER);
        }
        User savedUser = userRepository.save(user);

        try {
            emailService.sendWelcomeEmail(savedUser);
        } catch (Exception e) {
            System.out.println("Failed to send welcome email: " + e.getMessage());
        }
        return savedUser;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public User fetchUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public User fetchUsersByName(String name) {
        return userRepository.findUserByUserName(name);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public User fetchUsersByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public User updateUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User assignRole(Long userId, Role role) {
        User user = fetchUserById(userId);
        user.setRole(role);
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void DeleteUser(Long id) {
        List<Task> assignedTasks = taskRepository.findByAssigneeId(id);

        for (Task task : assignedTasks) {
            task.setAssignee(null);
            taskRepository.save(task);
        }

        userRepository.deleteById(id);
    }
}
