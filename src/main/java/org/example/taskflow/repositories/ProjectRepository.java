package org.example.taskflow.repositories;

import org.example.taskflow.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project getProjectsByName(String name);
}
