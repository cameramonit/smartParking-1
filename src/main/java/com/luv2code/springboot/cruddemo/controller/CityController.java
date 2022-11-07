package com.luv2code.springboot.cruddemo.controller;

import com.luv2code.springboot.cruddemo.repository.CityRepository;
import com.luv2code.springboot.cruddemo.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")

public class CityController {

    @Autowired
    CityRepository cityRepository;

    @GetMapping("/cities")
    public List<City> findAll(){
        return cityRepository.findAll();
    }

    @GetMapping("/cities/{id}")
    public Optional<City> byId(@PathVariable int id){

        return cityRepository.findById(id);
    }

    @PostMapping("/cities")
    public City create(@RequestBody City body){

        String name = body.getName();
        return cityRepository.save(new City(name));
    }

    @PutMapping("/cities/{id}")
    public ResponseEntity<City> updateCity(@PathVariable("id") int id, @RequestBody City city){
        Optional<City> cityData = cityRepository.findById(id);

        if (cityData.isPresent()){
            City city1 = cityData.get();
            city1.setName(city.getName());

            return new ResponseEntity<>(cityRepository.save(city1), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/cities/{id}")
    public ResponseEntity<HttpStatus> deleteCity(@PathVariable("id") int id){
        try {
            cityRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
