package banger.controller;

import banger.dto.CarDTO;
import banger.model.Car;
import banger.model.User;
import banger.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private static Logger logger = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private CarService service;

    @GetMapping("{id}")
    public ResponseEntity<Car> find(@PathVariable("id") String carId){
        Car car = service.find(carId);
        if (car == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(car);
    }

    @GetMapping
    public ResponseEntity<List<Car>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Car>> findAvailable(){
        List<Car> availableCars = service.findAvailable();
        return ResponseEntity.ok(availableCars);
    }

    @PostMapping("/admin/add")
    @Secured(User.ROLE_ADMIN)
    public ResponseEntity<Car> create(@Valid @RequestBody CarDTO c){
        Car car = service.create(c);
        if(car == null) {
            logger.error("Car is null");
            return ResponseEntity.badRequest().build();
        } else {
            logger.info("Created Car" + car.getLicensePlate());
            return ResponseEntity.ok(car);
        }
    }

    @GetMapping("/admin/delete/{id}")
    @Secured(User.ROLE_ADMIN)
    public ResponseEntity<String> deleteCar(@PathVariable String id){
        String response = service.deleteById(id);
        if(response == null){
            return ResponseEntity.badRequest().body("Nincs ilyen autó!");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @Secured(User.ROLE_ADMIN)
    public ResponseEntity<Car> update(@PathVariable("id") String carId, @Valid @RequestBody CarDTO update){
        Car car = service.update(carId, update);
        if(car == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(car);}
    }

}
