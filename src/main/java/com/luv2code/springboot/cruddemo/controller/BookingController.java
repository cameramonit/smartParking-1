package com.luv2code.springboot.cruddemo.controller;

import com.luv2code.springboot.cruddemo.repository.BookingRepository;
import com.luv2code.springboot.cruddemo.repository.SlotRepository;
import com.luv2code.springboot.cruddemo.entity.Booking;
import com.luv2code.springboot.cruddemo.entity.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class BookingController {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    SlotRepository slotRepository;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public List<Booking> all(){

        return bookingRepository.findAll();
    }

    @GetMapping("/bookings/{id}")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public Optional<Booking> byId(@PathVariable int id){

       // int bookingId = Integer.parseInt(id);

        return bookingRepository.findById(id);
    }

    @PostMapping("/slots/{slotId}/bookings")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Booking> createBooking(@PathVariable(value = "slotId") int slotId,
                                                 @RequestBody Booking bookingRequest) {
        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new ResourceNotFoundException("Not found slot with id = " + slotId));

        bookingRequest.setSlot(slot);
        return new ResponseEntity<>(bookingRepository.save(bookingRequest), HttpStatus.CREATED);
    }


    @PutMapping("/bookings/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Booking> updateBooking(@PathVariable("id") int id, @RequestBody Booking bookingRequest) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BookingId " + id + "not found"));

        booking.setLicensePlate(bookingRequest.getLicensePlate());
        booking.setEnterTime(bookingRequest.getEnterTime());
        booking.setExitTime(bookingRequest.getExitTime());

        return new ResponseEntity<>(bookingRepository.save(booking), HttpStatus.OK);
    }





    @DeleteMapping("/bookings/{id}")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable("id") int id){

        try{
            bookingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
