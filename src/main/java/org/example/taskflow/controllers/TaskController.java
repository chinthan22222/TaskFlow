package org.example.taskflow.controllers;

import org.example.taskflow.entities.Task;
import org.example.taskflow.enums.Status;
import org.example.taskflow.services.TaskServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskServices taskService;

    @PostMapping("/")
    public Task createTask(@RequestBody Task task) {
        return taskService.NewTask(task);
    }

    @GetMapping("/")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{taskId}/assign/{userId}")
    public Task assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        return taskService.assignTask(taskId, userId);
    }

    @PutMapping("/update")
    public Task updateTask(@RequestBody Task task) {
        return taskService.UpdateTask(task);
    }

    @PutMapping("/{taskId}/status")
    public Task changeTaskStatus(@PathVariable Long taskId, @RequestParam Status status) {
        Task task = taskService.fetchTaskById(taskId); // You can create/assume this method
        return taskService.ChangeTaskStatus(task, status);
    }

    @DeleteMapping("/delete/{taskid}")
    public void deletetask(@PathVariable("taskid") long id){
        this.taskService.DeleteTask(id);
    }

    @GetMapping("/me")
    public List<Task> getMyTasks() {
        return taskService.getMyTasks();
    }
}
