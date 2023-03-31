package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Parking;
import com.luv2code.springboot.cruddemo.entity.Slot;
import com.luv2code.springboot.cruddemo.repository.ParkingRepository;
import com.luv2code.springboot.cruddemo.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;


    public List<Slot> findByParkingId(int parkingId){
        return slotRepository.findByParkingId(parkingId);
    }

}
