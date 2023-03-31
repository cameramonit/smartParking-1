package com.luv2code.springboot.cruddemo.controller;

import com.luv2code.springboot.cruddemo.repository.BusinessRepository;
import com.luv2code.springboot.cruddemo.repository.CityRepository;
import com.luv2code.springboot.cruddemo.repository.ParkingRepository;
import com.luv2code.springboot.cruddemo.entity.*;
import com.luv2code.springboot.cruddemo.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    ParkingRepository parkingRepository;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    CityRepository cityRepository;

    @GetMapping("/parkings")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN')")
  //  @PreAuthorize("isAuthenticated()")
    public List<Parking> getAll(){

        return parkingRepository.findAll();
    }

    // get logged user
    @GetMapping("/loggedUser")
    public String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }


    // parkings that are associated with a business ID.
    @GetMapping("/business/{businessId}/parkings")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
   // @PreAuthorize("hasRole('SUPERADMIN')" + "and authentication.principal.equals(#businessId)")
    public List<Parking> findByBusinessId(@PathVariable int businessId, Principal principal) {

        //1. check if superadmin or admin
        //2. if principal is super admin
        //3. if businessid is not the same as principal business id, throw an error
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = principal.getName();
        Business business = businessRepository.findById(businessId).orElseThrow(() -> new ResourceNotFoundException("Not found business with id = " + businessId));

      Collection<? extends  GrantedAuthority> authorities =
                authentication.getAuthorities();
        for (GrantedAuthority authority : authorities){
            String role = authority.getAuthority();
            if (role.equals("ADMIN") && !username.equals(String.valueOf(business.getBusinessId()))){
                throw new RuntimeException("Error: authenticated principal does not match business ID");
            }
        }

        return parkingService.findByBusinessId(businessId);
    }

    @GetMapping("/parkings/{id}")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN')")
    public Optional<Parking> findById(@PathVariable int id){

        return parkingRepository.findById(id);
    }


    

    @PostMapping("/businesses/{businessId}/cities/{cityId}/parkings")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN')")
   // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Parking> updateParking(@PathVariable("id") int id, @RequestBody Parking parkingRequest) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ParkingId " + id + "not found"));

        parking.setName(parkingRequest.getName());
        parking.setLocation(parkingRequest.getLocation());
        parking.setSlotPrice(parkingRequest.getSlotPrice());


        return new ResponseEntity<>(parkingRepository.save(parking), HttpStatus.OK);
    }

    @DeleteMapping("/parkings/{id}")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN') ")
    public ResponseEntity<HttpStatus> deleteParking(@PathVariable("id") int id){
        try {
            parkingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
