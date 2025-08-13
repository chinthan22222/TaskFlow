package org.example.taskflow.services;

import org.example.taskflow.entities.Project;
import org.example.taskflow.entities.Task;
import org.example.taskflow.exceptions.ProjectNotFoundException;
import org.example.taskflow.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectServices {

    @Autowired
    private ProjectRepository projectRepository;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public List<Project> getAllProjects() {
        return this.projectRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public Project getProjectById(Long id) {
        Optional<Project> optional = projectRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ProjectNotFoundException("Project not found with ID: " + id);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public Project getProjectByName(String name) {
        return this.projectRepository.getProjectsByName(name);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public List<Task> getProjectTasks(Long projectId) {
        Optional<Project> optional = projectRepository.findById(projectId);
        if (optional.isPresent()) {
            return optional.get().getTasks();
        } else {
            throw new ProjectNotFoundException("Project not found with ID: " + projectId);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Project createProject(Project project) {
        return this.projectRepository.save(project);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Project updateProject(Long id, Project updatedProject) {
        Project existing = getProjectById(id);
        existing.setName(updatedProject.getName());
        existing.setDescription(updatedProject.getDescription());
        return this.projectRepository.save(existing);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String deleteProject(Long projectId) {
        Project project = getProjectById(projectId);
        projectRepository.delete(project);
        return "Project deleted successfully.";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public Map<String, Map<String, String>> ProjectTaskMembers() {
        List<Project> projects = projectRepository.findAll();
        Map<String, Map<String, String>> result = new HashMap<>();

        for (Project project : projects) {
            Map<String, String> taskUserMap = new HashMap<>();

            List<Task> tasks = project.getTasks();
            if (tasks != null) {
                for (Task task : tasks) {
                    String taskTitle = task.getTitle();
                    String assigneeName = (task.getAssignee() != null)
                            ? task.getAssignee().getUserName()
                            : "Unassigned";
                    taskUserMap.put(taskTitle, assigneeName);
                }
            }

            result.put(project.getName(), taskUserMap);
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    public Map<String, String> getProjectProgress() {
        List<Project> projects = projectRepository.findAll();
        Map<String, String> progressMap = new HashMap<>();

        for (Project project : projects) {
            List<Task> tasks = project.getTasks();
            if (tasks == null || tasks.isEmpty()) {
                progressMap.put(project.getName(), "0%");
            } else {
                long completed = tasks.stream()
                        .filter(task -> task.getStatus().toString().equals("COMPLETED"))
                        .count();
                int percentage = (int) ((double) completed / tasks.size() * 100);
                progressMap.put(project.getName(), percentage + "%");
            }
        }

        return progressMap;
    }
}
