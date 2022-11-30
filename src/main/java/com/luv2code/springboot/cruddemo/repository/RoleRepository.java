package com.luv2code.springboot.cruddemo.repository;

import com.luv2code.springboot.cruddemo.entity.ERole;
import com.luv2code.springboot.cruddemo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);
}
