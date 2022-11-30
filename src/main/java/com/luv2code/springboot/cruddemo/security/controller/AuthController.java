package com.luv2code.springboot.cruddemo.security.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.luv2code.springboot.cruddemo.entity.Business;
import com.luv2code.springboot.cruddemo.entity.ERole;
import com.luv2code.springboot.cruddemo.entity.Role;
import com.luv2code.springboot.cruddemo.entity.User;
import com.luv2code.springboot.cruddemo.repository.BusinessRepository;
import com.luv2code.springboot.cruddemo.repository.RoleRepository;
import com.luv2code.springboot.cruddemo.repository.UserRepository;
import com.luv2code.springboot.cruddemo.security.jwt.JwtUtils;
import com.luv2code.springboot.cruddemo.security.payload.MessageResponse;
import com.luv2code.springboot.cruddemo.security.payload.UserInfoResponse;
import com.luv2code.springboot.cruddemo.security.payload.request.LoginRequest;
import com.luv2code.springboot.cruddemo.security.payload.request.SignUpRequest;
import com.luv2code.springboot.cruddemo.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    BusinessRepository businessRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE)
                .body(new UserInfoResponse(Math.toIntExact(userDetails.getId()),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles, jwtCookie.getValue()
                        ));


    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        if(businessRepository.existsByName(signUpRequest.getName())){
            return new ResponseEntity<>("Name is already taken", HttpStatus.BAD_REQUEST);
        }

        if (businessRepository.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }


        Business b1 = new Business();
        b1.setName(signUpRequest.getName());
        b1.setEmail(signUpRequest.getEmail());
        b1.setFoundationDate(signUpRequest.getFoundationDate());
        b1.setRegistrationDate(signUpRequest.getRegistrationDate());

        Business business = businessRepository.save(b1);



        // Create new user's account
         User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setUsername(signUpRequest.getUsername());


        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        Role businessAdmin = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(businessAdmin);

        user.setBusiness(business);
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
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



    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
}




