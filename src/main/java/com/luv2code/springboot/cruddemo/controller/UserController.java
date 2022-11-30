package com.luv2code.springboot.cruddemo.controller;

import com.luv2code.springboot.cruddemo.entity.*;
import com.luv2code.springboot.cruddemo.repository.BusinessRepository;
import com.luv2code.springboot.cruddemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    BusinessRepository businessRepository;
    @Autowired
    UserRepository userRepository;

    // Get all users

    @GetMapping("/users")
    public List<User> all(){
        return userRepository.findAll();
    }

    // Get a single user

    @GetMapping("/users/{id}")
    public Optional<User> byId(@PathVariable int id){

        return userRepository.findById(id);
    }

    // Create user

    @PostMapping("/businesses/{businessId}/users")
    public ResponseEntity<User> createUser(@PathVariable(value = "businessId") int businessId,
                                                 @RequestBody User userRequest) {
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new ResourceNotFoundException("Not found business with id = " + businessId));

        userRequest.setBusiness(business);

        return new ResponseEntity<>(userRepository.save(userRequest), HttpStatus.CREATED);
    }

    // Update user

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserId " + id + "not found"));


        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());



        return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
    }

    // Delete user

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id){

        try{
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
