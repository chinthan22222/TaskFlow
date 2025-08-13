package org.example.taskflow.controllers;

import org.example.taskflow.entities.User;
import org.example.taskflow.enums.Role;
import org.example.taskflow.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServices userServices;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userServices.registerUser(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userServices.getAllUsers();
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userServices.fetchUserById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/username/{name}")
    public User getUserByName(@PathVariable String name) {
        return userServices.fetchUsersByName(name);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userServices.fetchUsersByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id); // Ensure the ID is set from path variable
        return userServices.updateUser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public User assignRole(@PathVariable Long id, @RequestParam Role role) {
        return userServices.assignRole(id, role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userServices.DeleteUser(id);
        return "User deleted successfully";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    @GetMapping("/profile")
    public User getCurrentUserProfile(Authentication authentication) {
        String username = authentication.getName();
        return userServices.fetchUsersByName(username);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    @GetMapping("/dashboard")
    public Map<String, Object> getUserDashboard(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userServices.fetchUsersByName(username);

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("user", currentUser);
        dashboard.put("role", currentUser.getRole().toString());
        dashboard.put("username", currentUser.getUserName());
        dashboard.put("email", currentUser.getEmail());

        switch (currentUser.getRole()) {
            case ADMIN:
                dashboard.put("totalUsers", userServices.getAllUsers().size());
                dashboard.put("permissions", "Full system access");
                break;
            case MANAGER:
                dashboard.put("permissions", "Project and user management");
                break;
            case DEVELOPER:
                dashboard.put("permissions", "Task management and updates");
                break;
        }

        return dashboard;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEVELOPER')")
    @PutMapping("/change-password")
    public String changePassword(
            @RequestParam String newPassword,
            Authentication authentication) {

        String username = authentication.getName();
        User currentUser = userServices.fetchUsersByName(username);
        currentUser.setPassword(newPassword);
        userServices.updateUser(currentUser);

        return "Password updated successfully";
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/role/{role}")
    public List<User> getUsersByRole(@PathVariable Role role) {
        return userServices.getAllUsers().stream()
                .filter(user -> user.getRole() == role)
                .toList();
    }
}
