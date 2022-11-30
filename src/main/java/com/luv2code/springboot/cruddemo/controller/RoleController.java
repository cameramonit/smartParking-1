package com.luv2code.springboot.cruddemo.controller;

import com.luv2code.springboot.cruddemo.entity.Role;
import com.luv2code.springboot.cruddemo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/roles")
    public List<Role> showAll(){
        return roleRepository.findAll();
    }

    @GetMapping("/roles/{id}")
    public Optional<Role> findbyId(@PathVariable int id){
        return roleRepository.findById(id);
    }
}
