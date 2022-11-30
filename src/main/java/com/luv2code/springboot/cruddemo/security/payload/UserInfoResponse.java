package com.luv2code.springboot.cruddemo.security.payload;

import org.springframework.http.ResponseCookie;

import java.util.List;

public class UserInfoResponse {
    private Integer id;
    private String username;
    private String email;
    private List<String> roles;

    private String jwt;


    public UserInfoResponse(Integer id, String username, String email, List<String> roles, String jwt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.jwt= jwt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getJwt() {
        return jwt;
    }


    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "jwt=" + jwt +
                '}';
    }
}
