package com.luv2code.springboot.cruddemo.security;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String password;
    private String email;
}
