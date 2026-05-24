package com.renteasy.renteasy.repository;


import java.util.Optional;
import com.renteasy.renteasy.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}