package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Parking;
import com.luv2code.springboot.cruddemo.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingService{

    @Autowired
    private ParkingRepository parkingRepository;

    public List<Parking> findByBusinessId(int businessId) {
        return parkingRepository.findByBusinessId(businessId);
    }

}
