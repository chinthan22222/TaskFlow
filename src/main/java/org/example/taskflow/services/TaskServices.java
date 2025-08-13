package org.example.taskflow.services;

import org.example.taskflow.entities.Project;
import org.example.taskflow.entities.Task;
import org.example.taskflow.entities.User;
import org.example.taskflow.enums.Status;
import org.example.taskflow.exceptions.TaskNotFoundException;
import org.example.taskflow.exceptions.UserNotFoundException;
import org.example.taskflow.repositories.ProjectRepository;
import org.example.taskflow.repositories.TaskRepository;
import org.example.taskflow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServices {

    @Autowired
    public TaskRepository taskRepository;

    @Autowired
    public UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ProjectRepository projectRepository;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Task NewTask(Task task) {
        if (task.getProject() == null || task.getProject().getId() == null) {
            throw new IllegalArgumentException("Project is required when creating a task");
        }

        Project project = projectRepository.findById(task.getProject().getId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + task.getProject().getId()));
        task.setProject(project);

        if (task.getAssignee() != null && task.getAssignee().getId() != 0.0) {
            User assignee = userRepository.findById(task.getAssignee().getId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + task.getAssignee().getId()));
            task.setAssignee(assignee);
        }
        Task savedTask = taskRepository.save(task);

        if (savedTask.getAssignee() != null) {
            try {
                emailService.sendTaskAssignmentEmail(savedTask.getAssignee(), savedTask);
            } catch (Exception e) {
                System.err.println("Failed to send task assignment email: " + e.getMessage());
            }
        }

        return savedTask;
    }


    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Task assignTask(long taskId, long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found for ID: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId));

        if (task.getStatus() == Status.COMPLETED) {
            throw new IllegalStateException("Cannot assign completed task");
        }

        if (task.getAssignee() != null && task.getAssignee().getId() == userId) {
            throw new IllegalStateException("Task is already assigned to user: " + user.getUserName());
        }

        task.setAssignee(user);
        Task savedTask = taskRepository.save(task);

        try {
            emailService.sendTaskAssignmentEmail(user, savedTask);
        } catch (Exception e) {
            System.err.println("Email notification failed: " + e.getMessage());
        }

        return savedTask;
    }


    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public Task UpdateTask(Task task) {
        return this.taskRepository.save(task);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public Task ChangeTaskStatus(Task task, Status newStatus) {
        task.setStatus(newStatus);
        return this.taskRepository.save(task);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public Task fetchTaskById(long id){
        Optional<Task> optional1 = taskRepository.findById(id);
        if (optional1.isEmpty()) {
            throw new RuntimeException("Task not found for ID: " + id);
        }
        Task task = optional1.get();
        return task;
    }


    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public void DeleteTask(Long id){
        this.taskRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public List<Task> getMyTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated: no current user context available");
        }
        String username = authentication.getName();
        User user = userRepository.findUserByUserName(username);
        if (user == null) {
            throw new UserNotFoundException("User not found for username: " + username);
        }
        return taskRepository.findByAssigneeId(user.getId());
    }
}
