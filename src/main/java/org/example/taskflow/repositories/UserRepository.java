package org.example.taskflow.repositories;

import org.example.taskflow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllUserByUserName(String userName);
    User findUserByEmail(String Email);
    User findUserByUserName(String userName);
    Optional<User> findByUserName(String userName);

    User findUserById(long id);
}
