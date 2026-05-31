package com.renteasy.renteasy.repository;

import com.renteasy.renteasy.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"role"})
    List<User> findAll();

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findById(Long id);
}
