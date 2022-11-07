package com.luv2code.springboot.cruddemo.security.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Data
public class BusinessSignUpDTO {

    private String name;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date foundationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date registrationDate;
    private int businessNumber;
    private Integer status;
    private String comment;
}
