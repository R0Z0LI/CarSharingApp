package banger.service;

import banger.dto.RentalDTO;
import banger.model.Car;
import banger.model.Rental;
import banger.model.Site;
import banger.model.User;
import banger.repository.CarRepository;
import banger.repository.RentalRepository;
import banger.repository.SiteRepository;
import banger.repository.UserRepository;
import banger.service.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    Transformer transformer;

    private static Logger logger = LoggerFactory.getLogger(RentalService.class);

    @Transactional
    public Rental addRental(String carId, String userId){
        logger.debug("addRental: " + carId + " " + userId);
        Optional<User> oRenter = userRepository.findById(userId);
        if(oRenter.isEmpty()){
            logger.error("user does not exist");
            return null;
        }
        logger.info("user found: " + userId);
        Optional<Car> oCar = carRepository.findById(carId);
        if(oCar.isEmpty()) {
            logger.error("car does not exist");
            return null;
        }
        logger.info("car found: " + carId);
        Car car = oCar.get();
        if(car.getRenter() != null){
            logger.error("someone already renting car "+carId);
            return null;
        }
        logger.info("renter found");
        User user = oRenter.get();
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCar(car);
        rental.setFrom(LocalDateTime.now());
        rental.setTo(null);
        car.setRenter(user);
        Site s = car.getSite();
        s.getAvailableCars().remove(car);
        car.setSite(null);
        siteRepository.save(s);
        carRepository.save(car);
        logger.info("car saved: " + carId);
        rentalRepository.save(rental);
        logger.info("rental saves " + rental.getId());
        return rental;
    }

    @Transactional
    public Rental endRental(String rentalId, String siteId){
        logger.debug("ending rental: " + rentalId + " " + siteId);
        Optional<Rental> oRental = rentalRepository.findById(rentalId);
        if(oRental.isEmpty()) {
            logger.error("rental does not exist");
            return null;
        }
        logger.info("rental found: " + rentalId);
        Optional<Site> oSite = siteRepository.findById(siteId);
        if(oSite.isEmpty()) {
            logger.error("site does not found");
            return null;
        }
        logger.info("site found: " + siteId);
        Rental r = oRental.get();
        r.setTo(LocalDateTime.now());
        Car c = r.getCar();
        c.setRenter(null);
        Site s = oSite.get();
        s.getAvailableCars().add(c);
        c.setSite(s);
        siteRepository.save(s);
        logger.info("site saved: " + siteId);
        carRepository.save(c);
        logger.info("car saved: " + c.getLicensePlate());
        rentalRepository.save(r);
        logger.info("rental saved: " + rentalId);
        return r;
    }

    public Rental find(String rentalId) {
        logger.debug("finding by id: " + rentalId);
        Optional<Rental> oRental = rentalRepository.findById(rentalId);
        if(oRental.isEmpty()) {
            logger.error("rental does not exist");
            return null;
        }
        Rental rental = oRental.get();
        logger.info("rental found");
        logger.debug("rental: " + rental.getId());
        return rental;
    }

    public List<Rental> findInactiveByUserID(String userID) {
        logger.debug("find inactive rentals by userId: " + userID);
        Optional<User> user = userRepository.findById(userID);
        if(user.isPresent()){
            List<Rental> rentals = rentalRepository.getByUserAndToIsNotNull(user.get());
            logger.info("rentals found");
            logger.debug(rentals.toString());
            return rentals;
        } else {
            logger.error("user does not exist");
            return null;
        }
    }



    public Rental update(String rentalId, RentalDTO update) {
        logger.debug("update: " + rentalId + " " + update);
        Optional<Rental> oRental = rentalRepository.findById(rentalId);
        if(oRental.isEmpty()) {
            logger.error("rental does not exist");
            return null;
        }
        logger.info("rental found");
        Rental rental = oRental.get();
        rental = transformer.updateRental(rental, update);
        logger.info("rental transformed");
        rentalRepository.save(rental);
        logger.info("rental saved");
        logger.debug(rental.toString());
        return rental;
    }

    public List<Rental> findAll() {
        logger.info("find all");
        List<Rental> rentals = rentalRepository.findAll();
        logger.debug(rentals.toString());
        return rentals;
    }

    public List<Rental> findActiveByUserID(String userID) {
        logger.debug("find active rentals by userId: " + userID);
        Optional<User> user = userRepository.findById(userID);
        if(user.isPresent()){
            List<Rental> rentals = rentalRepository.getByUserAndToIsNull(user.get());
            logger.info("rentals found");
            ResponseEntity<List<Rental>> responseEntity = ResponseEntity.ok(rentals);
            logger.debug(responseEntity.toString());
            return rentals;
        } else {
            logger.error("user does not exist");
            return null;
        }
    }
}
