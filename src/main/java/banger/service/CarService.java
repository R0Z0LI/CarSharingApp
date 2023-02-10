package banger.service;

import banger.dto.CarDTO;
import banger.model.Car;
import banger.repository.CarRepository;
import banger.service.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    @Autowired
    private CarRepository repository;
    @Autowired
    private Transformer transformer;

    private static Logger logger = LoggerFactory.getLogger(CarService.class);

    public Car create(CarDTO c){
        logger.info("creating car");
        if(repository.existsById(c.getLicensePlate())){
            logger.error("car already exists: "+c.getLicensePlate());
            return null;
        }
        Car car = transformer.transform(c);
        if(car == null){
            logger.error("error while creating car (non-existent site/category)");
            return null;
        }
        logger.info("carDTO transformed to Car");
        repository.save(car);
        return car;
    }

    public Car find(String carId) {
        logger.debug("finding by id: " + carId);
        Optional<Car> oCar = repository.findById(carId);
        if(oCar.isEmpty()){
            logger.error("car does not exist");
            return null;
        }
        Car car = oCar.get();
        logger.info("found car by id: " + car.getLicensePlate());
        return car;
    }

    public List<Car> findAll() {
        logger.info("find all cars");
        List<Car> cars = repository.findAll();
        logger.info("found: "+cars);
        return cars;
    }

    public String deleteById(String id) {
        logger.debug("deleting by id: " + id);
        if(!repository.existsById(id)) {
            logger.error("car does not exist");
            return null;
        }
        repository.deleteById(id);
        logger.info("deletion successful");
        return "Sikeres autó törlés!";
    }

    public Car update(String carId, CarDTO update) {
        logger.debug("car licenseplate: " + update.getLicensePlate() + " car's categoryId: " + update.getCategoryId());
        Optional<Car> oCar = repository.findById(carId);
        if(oCar.isEmpty()) {
            logger.error("car not found");
            return null;
        }
        logger.info("found car to update");
        Car car = oCar.get();
        car = transformer.updateCar(car, update);
        if(car == null){
            logger.error("error while updating car (non-existent site/category)");
            return null;
        }
        logger.info("CarDTO transformed to Car");
        repository.save(car);
        logger.info("updated successfully");
        logger.debug("updated car: " + car.getLicensePlate());
        return car;
    }

    public List<Car> findAvailable() {
        logger.info("finding available cars");
        return repository.findByRenterIsNull();
    }
}



