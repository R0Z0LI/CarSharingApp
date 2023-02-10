package banger.controller;

import banger.dto.RentalDTO;
import banger.model.Rental;
import banger.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    @Autowired
    RentalService service;

    @GetMapping("{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable("id") String rentalId){
        Rental rental = service.find(rentalId);
        if(rental == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(rental);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Rental> addRental(@RequestParam("carId") String carId, @RequestParam("userId") String userId){
        Rental rental = service.addRental(carId, userId);
        if(rental == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(rental);
        }
    }

    @PostMapping("/endRental")
    public ResponseEntity<Rental> endRental(@RequestParam("rentalId") String rentalId, @RequestParam("siteId") String siteId){
        Rental rental = service.endRental(rentalId, siteId);
        if(rental == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(rental);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Rental>> findActive(@RequestParam("userId") String userID){
        List<Rental> rentals = service.findActiveByUserID(userID);
        if(rentals == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(rentals);
        }
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Rental>> findAllForUser(@RequestParam("userId") String userID){
        List<Rental> rentals = service.findInactiveByUserID(userID);
        if(rentals == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(rentals);
        }
    }

    @GetMapping
    public ResponseEntity<List<Rental>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Rental> update(@PathVariable("id") String carId, @Valid @RequestBody RentalDTO update){
        Rental rental = service.update(carId, update);
        if(rental == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(rental);
        }
    }
}
