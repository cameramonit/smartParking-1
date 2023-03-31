package com.luv2code.springboot.cruddemo.controller;

import com.luv2code.springboot.cruddemo.repository.ParkingRepository;
import com.luv2code.springboot.cruddemo.repository.SlotRepository;
import com.luv2code.springboot.cruddemo.entity.Parking;
import com.luv2code.springboot.cruddemo.entity.Slot;
import com.luv2code.springboot.cruddemo.service.SlotService;
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

public class SlotController {


    @Autowired
    private SlotService slotService;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    ParkingRepository parkingRepository;

    @GetMapping("/slots")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public List<Slot> findAll(){

        return slotRepository.findAll();
    }

    @GetMapping("/slots/{id}")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public Optional<Slot> findById(@PathVariable int id){

        return slotRepository.findById(id);
    }

    @GetMapping("/parking/{parkingId}/slots")
    public List<Slot> findByParkingId(@PathVariable int parkingId){
        return slotService.findByParkingId(parkingId);
    }

    @PostMapping("/parkings/{parkingId}/slots")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN')")
    public ResponseEntity<Slot> createSlot(@PathVariable(value = "parkingId") int parkingId,
                                                 @RequestBody Slot slotRequest) {
        Parking parking = parkingRepository.findById(parkingId).orElseThrow(() -> new ResourceNotFoundException("Not found parking with id = " + parkingId));

        slotRequest.setParking(parking);
        return new ResponseEntity<>(slotRepository.save(slotRequest), HttpStatus.CREATED);
    }


    @PutMapping("/slots/{id}")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasRole('SUPERVISOR')")
    public ResponseEntity<Slot> updateSlot(@PathVariable("id") int id, @RequestBody Slot slotRequest) {
        Slot slot = slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slot " + id + "not found"));

        slot.setNumber(slotRequest.getNumber());
        slot.setLocation(slotRequest.getLocation());
        slot.setStatus(slotRequest.getStatus());

        return new ResponseEntity<>(slotRepository.save(slot), HttpStatus.OK);
    }

    @DeleteMapping("/slots/{id}")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteSlot(@PathVariable("id") int id){

        try{
            slotRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
