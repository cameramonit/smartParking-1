package com.luv2code.springboot.cruddemo.repository;

import com.luv2code.springboot.cruddemo.entity.Parking;
import com.luv2code.springboot.cruddemo.entity.Slot;
import com.luv2code.springboot.cruddemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository <Slot, Integer> {

   // public List<Slot>findSlotByStatusIs(int status);


    List<Slot> findByParkingId(int parkingId);

}
