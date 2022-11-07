package com.luv2code.springboot.cruddemo.controller;

import com.luv2code.springboot.cruddemo.repository.BusinessRepository;
import com.luv2code.springboot.cruddemo.entity.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class BusinessController {

    @Autowired
    BusinessRepository businessRepository;

    @GetMapping("/businesses")
    public List<Business> all(){

       return businessRepository.findAll();
    }

    @GetMapping("/businesses/{id}")
    public Optional<Business> byId(@PathVariable int id){

        // int bookingId = Integer.parseInt(id);

        return businessRepository.findById(id);
    }

    @PostMapping("/businesses")
    public Business create(@RequestBody Business body){

        String name = body.getName();
        String email = body.getEmail();
        Date foundationDate = body.getFoundationDate();
        Date registrationDate = body.getRegistrationDate();
        int businessNumber = body.getBusinessNumber();
        int status = body.getStatus();
        String comment = body.getComment();


        return businessRepository.save(new Business(name, email, foundationDate, registrationDate, businessNumber, status, comment));
    }


    @PutMapping("/businesses/{id}")
    public ResponseEntity<Business> updateBusiness(@PathVariable("id") int id, @RequestBody Business business) {
        Optional<Business> businessData = businessRepository.findById(id);

        if (businessData.isPresent()) {
            Business business1 = businessData.get();
            business1.setName(business.getName());
            business1.setEmail(business.getEmail());
            business1.setBusinessNumber(business.getBusinessNumber());
            business1.setFoundationDate(business.getFoundationDate());
            business1.setRegistrationDate(business.getRegistrationDate());
            business1.setStatus(business.getStatus());
            business1.setComment(business.getComment());

            return new ResponseEntity<>(businessRepository.save(business1), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/businesses/{id}")
    public ResponseEntity<HttpStatus> deleteBusiness(@PathVariable("id") int id){

        try{
            businessRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    


}
