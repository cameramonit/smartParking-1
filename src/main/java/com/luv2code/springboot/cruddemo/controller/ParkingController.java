package com.luv2code.springboot.cruddemo.controller;

import com.luv2code.springboot.cruddemo.repository.BusinessRepository;
import com.luv2code.springboot.cruddemo.repository.CityRepository;
import com.luv2code.springboot.cruddemo.repository.ParkingRepository;
import com.luv2code.springboot.cruddemo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class ParkingController {

    @Autowired
    ParkingRepository parkingRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    CityRepository cityRepository;

    @GetMapping("/parkings")
    public List<Parking> getAll(){
        return parkingRepository.findAll();
    }


    @GetMapping("/parkings/{id}")
    public Optional<Parking> findById(@PathVariable int id){
        return parkingRepository.findById(id);
    }


    @PostMapping("/businesses/{businessId}/cities/{cityId}/parkings")
    public ResponseEntity<Parking> createParking(@PathVariable(value = "businessId") int businessId,
                                                 @PathVariable(value = "cityId") int cityId,
                                                 @RequestBody Parking parkingRequest
                                                 ) {
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new ResourceNotFoundException("Not found business with id = " + businessId));
        City city = cityRepository.findById(cityId).orElseThrow(() -> new ResourceNotFoundException("Not found city with id = " + cityId));

        parkingRequest.setBusiness(business);
        parkingRequest.setCity(city);

        return new ResponseEntity<>(parkingRepository.save(parkingRequest), HttpStatus.CREATED);
    }

    @PutMapping("/parkings/{id}")
    public ResponseEntity<Parking> updateParking(@PathVariable("id") int id, @RequestBody Parking parkingRequest) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParkingId " + id + "not found"));

        parking.setName(parkingRequest.getName());
        parking.setLocation(parkingRequest.getLocation());
        parking.setSlotPrice(parkingRequest.getSlotPrice());


        return new ResponseEntity<>(parkingRepository.save(parking), HttpStatus.OK);
    }

    @DeleteMapping("/parkings/{id}")
    public ResponseEntity<HttpStatus> deleteParking(@PathVariable("id") int id){
        try {
            parkingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
