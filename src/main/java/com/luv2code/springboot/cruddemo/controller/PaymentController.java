package com.luv2code.springboot.cruddemo.controller;

import com.luv2code.springboot.cruddemo.entity.*;
import com.luv2code.springboot.cruddemo.repository.BookingRepository;
import com.luv2code.springboot.cruddemo.repository.PaymentRepository;
import com.luv2code.springboot.cruddemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/payments")
    public List<Payment> getAll(){
        return paymentRepository.findAll();
    }

    @GetMapping("/payments/{id}")
    public Optional<Payment> findById(@PathVariable int id){
        return paymentRepository.findById(id);
    }

    @PostMapping("/bookings/{bookingId}/users/{userId}/payments")
    public ResponseEntity<Payment> createPayment(@PathVariable(value = "bookingId") int bookingId,
                                                 @PathVariable(value = "userId") int userId,
                                                 @RequestBody Payment paymentRequest
    ) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Not found booking with id = " + bookingId));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + userId));

        paymentRequest.setBooking(booking);
        paymentRequest.setUser(user);

        return new ResponseEntity<>(paymentRepository.save(paymentRequest), HttpStatus.CREATED);
    }

    @PutMapping("/payments/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable("id") int id, @RequestBody Payment paymentRequest) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentId " + id + "not found"));

        payment.setAmountDue(paymentRequest.getAmountDue());
        payment.setAmountPaid(paymentRequest.getAmountPaid());
        payment.setPaymentStatus(paymentRequest.getPaymentStatus());
        payment.setDatePaid(paymentRequest.getDatePaid());
        payment.setRemarks(paymentRequest.getRemarks());



        return new ResponseEntity<>(paymentRepository.save(payment), HttpStatus.OK);
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<HttpStatus> deletePayment(@PathVariable("id") int id){
        try {
            paymentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
