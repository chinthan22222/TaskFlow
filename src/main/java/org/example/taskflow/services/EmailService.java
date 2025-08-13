package org.example.taskflow.services;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.example.taskflow.entities.User;
import org.example.taskflow.entities.Task;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @PermitAll
    public void sendWelcomeEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome to TaskFlow!");
        message.setText("Hi " + user.getUserName() + ",\n\n" +
                "• User ID: "+user.getId()+"\n"+"• User Name: "+user.getUserName()+"\n"+"• Role: "+user.getRole()+"\n\n"+
                "Welcome to TaskFlow! Your account has been successfully created.\n\n" +
                "Best regards,\nTaskFlow Team");

        mailSender.send(message);
    }

    @PermitAll
    public void sendTaskAssignmentEmail(User user, Task task) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("New Task Assigned: " + task.getTitle());
        message.setText("Hi " + user.getUserName() + ",\n\n" +
                "You have been assigned a new task:\n\n" +
                "project: "+task.getProject().getName()+"\n"+
                "Description: "+task.getProject().getDescription()+"\n"+
                "Task: " + task.getTitle() + "\n" +
                "Description: " + task.getDescription() + "\n" +
                "Priority: " + task.getPriority() + "\n" +
                "Status: " + task.getStatus() + "\n\n" +
                "Please log in to TaskFlow to view more details.\n\n" +
                "Best regards,\nTaskFlow Team");

        mailSender.send(message);
    }
}
