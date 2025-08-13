package org.example.taskflow.config;

import org.example.taskflow.entities.Project;
import org.example.taskflow.entities.Task;
import org.example.taskflow.entities.User;
import org.example.taskflow.enums.Priority;
import org.example.taskflow.enums.Role;
import org.example.taskflow.enums.Status;
import org.example.taskflow.repositories.ProjectRepository;
import org.example.taskflow.repositories.TaskRepository;
import org.example.taskflow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String sampleProjectName = "Sample Project";
        Project existing = projectRepository.getProjectsByName(sampleProjectName);
        if (existing == null) {
            Project project = new Project(sampleProjectName, "A demo project created at startup for quick testing.");
            project = projectRepository.save(project);

            Task t1 = new Task();
            t1.setTitle("Set up repository");
            t1.setDescription("Initialize repo structure and add README");
            t1.setStatus(Status.OPEN);
            t1.setPriority(Priority.HIGH);
            t1.setAssignee(null);
            t1.setProject(project);

            Task t2 = new Task();
            t2.setTitle("Create first API endpoint");
            t2.setDescription("Implement basic health check endpoint");
            t2.setStatus(Status.OPEN);
            t2.setPriority(Priority.MEDIUM);
            t2.setAssignee(null);
            t2.setProject(project);

            taskRepository.save(t1);
            taskRepository.save(t2);

            System.out.println("[DataInitializer] Sample project and tasks created.");
        } else {
            System.out.println("[DataInitializer] Sample data already present. Skipping.");
        }

        String adminEmail = "admin@taskflow.local";
        String adminUsername = "admin";
        User existingAdminByEmail = userRepository.findUserByEmail(adminEmail);
        User existingAdminByUsername = userRepository.findUserByUserName(adminUsername);
        if (existingAdminByEmail == null && existingAdminByUsername == null) {
            User admin = new User();
            admin.setUserName(adminUsername);
            admin.setEmail(adminEmail);
            admin.setRole(Role.ADMIN);
            admin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(admin);
            System.out.println("[DataInitializer] Admin user created: username='" + adminUsername + "', email='" + adminEmail + "'.");
        } else {
            System.out.println("[DataInitializer] Admin user already exists. Skipping creation.");
        }
    }
}
