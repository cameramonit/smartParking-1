package com.luv2code.springboot.cruddemo.repository;

import com.luv2code.springboot.cruddemo.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
}
