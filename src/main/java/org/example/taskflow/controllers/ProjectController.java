package org.example.taskflow.controllers;

import org.example.taskflow.entities.Project;
import org.example.taskflow.entities.Task;
import org.example.taskflow.services.ProjectServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectServices projectService;

    @PostMapping("/")

    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @GetMapping("/")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("/name/{name}")
    public Project getProjectByName(@PathVariable String name) {
        return projectService.getProjectByName(name);
    }

    @GetMapping("/{id}/tasks")
    public List<Task> getProjectTasks(@PathVariable Long id) {
        return projectService.getProjectTasks(id);
    }

    @GetMapping("/progress")
    public Map<String, String> getProjectProgress(){return this.projectService.getProjectProgress();}

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        return projectService.updateProject(id, updatedProject);
    }

    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id) {
        return projectService.deleteProject(id);
    }

    @GetMapping("/task-members")
    public Map<String, Map<String, String>> getProjectTaskAssigneeMap() {
        return projectService.ProjectTaskMembers();
    }

}
