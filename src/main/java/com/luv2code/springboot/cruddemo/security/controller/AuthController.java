package com.luv2code.springboot.cruddemo.security.controller;

import com.luv2code.springboot.cruddemo.entity.Business;
import com.luv2code.springboot.cruddemo.entity.Parking;
import com.luv2code.springboot.cruddemo.entity.User;
import com.luv2code.springboot.cruddemo.repository.BusinessRepository;
import com.luv2code.springboot.cruddemo.repository.UserRepository;
import com.luv2code.springboot.cruddemo.security.UserLoginDTO;
import com.luv2code.springboot.cruddemo.security.UserSignUpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping()
    public String home(){
        return "Hello";
    }

  /*  @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody UserLoginDTO user) throws Exception {

        Authentication authObject = null;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authObject);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials");
        }

        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

   */

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody UserLoginDTO loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
    }



    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserSignUpDTO signUpDto) {

        // add check for username exists in a DB
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        if(businessRepository.existsByName(signUpDto.getName())){
            return new ResponseEntity<>("Name is already taken", HttpStatus.BAD_REQUEST);
        }

        if (businessRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        Business b1 = new Business();
        b1.setName(signUpDto.getName());
        b1.setEmail(signUpDto.getEmail());
        b1.setFoundationDate(signUpDto.getFoundationDate());
        b1.setRegistrationDate(signUpDto.getRegistrationDate());

        Business business = businessRepository.save(b1);
        User user = new User();
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(signUpDto.getPassword());
        user.setUsername(signUpDto.getUsername());

        user.setBusiness(business);

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }

    @PutMapping("/business/{id}/approve")
    public ResponseEntity<Business> approveBusiness(@PathVariable("id") int id, @RequestBody Business businessRequest) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BusinessId " + id + "not found"));

        business.setStatus(businessRequest.getStatus());
        return new ResponseEntity<>(businessRepository.save(business), HttpStatus.OK);
    }

    @PutMapping("/business/{id}/reject")
    public ResponseEntity<Business> rejectBusiness(@PathVariable("id") int id, @RequestBody Business businessRequest) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BusinessId " + id + "not found"));

        business.setStatus(businessRequest.getStatus());
        business.setComment(businessRequest.getComment());
        return new ResponseEntity<>(businessRepository.save(business), HttpStatus.OK);
    }
}
