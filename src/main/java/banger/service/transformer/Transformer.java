package banger.service.transformer;

import banger.dto.*;
import banger.model.*;
import banger.repository.CarRepository;
import banger.repository.CategoryRepository;
import banger.repository.SiteRepository;
import banger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static banger.service.UserService.ADMIN_KEY;

@Service
public class Transformer {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SiteRepository siteRepository;

    //USER

    public User transform(UserDTO userDTO){
        User user = new User();
        user = updateUser(user, userDTO);
        user.setAdmin(ADMIN_KEY.equals(userDTO.getAdminKey()));
        user.setEnabled(false);
        return user;
    }

    public User updateUser(User u, UserDTO update){
        u.setUsername(update.getUsername());
        u.setName(update.getName());
        u.setLicenseNumber(update.getLicenseNumber());
        u.setPassword(passwordEncoder.encode(update.getPassword()));
        u.setPhone(update.getPhone());
        u.setEmail(update.getEmail());
        return u;
    }

    //CATEGORY

    public Category transform(CategoryDTO categoryDTO){
        Category category = new Category();
        updateCategory(category, categoryDTO);
        return category;
    }

    public Category updateCategory(Category category, CategoryDTO categoryDTO) {
        category.setName(categoryDTO.getName());
        category.setPricePerHour(categoryDTO.getPricePerHour());
        return category;
    }

    //CAR

    public Car updateCar(Car car, CarDTO update) {
        Optional<Category> oCat = categoryRepository.findById(update.getCategoryId());
        if(oCat.isEmpty()) {
            return null;
        } else {
            car.setCategory(oCat.get());
        }
        if(update.getSiteId() == null || update.getSiteId().isEmpty()){
            car.setSite(null);
        } else {
            Optional<Site> oSite = siteRepository.findById(update.getSiteId());
            if (oSite.isEmpty()) {
                return null;
            } else {
                car.setSite(oSite.get());
            }
        }
        car.setLicensePlate(update.getLicensePlate());
        car.setManufacturer(update.getManufacturer());
        car.setModel(update.getModel());
        car.setSpace(update.getSpace());
        car.setYearProduced(update.getYearProduced());
        return car;
    }

    public Car transform(CarDTO carDTO){
        Car car = new Car();
        car = updateCar(car, carDTO);
        return car;
    }

    //SITE

    public Site updateSite(Site site, SiteDTO update) {
        ArrayList<Car> availableCars = new ArrayList<>();
        if(update.getAvailableCars() != null) {
            for (String carId : update.getAvailableCars()) {
                Optional<Car> oCar = carRepository.findById(carId);
                if (oCar.isEmpty()) return null;
                availableCars.add(oCar.get());
            }
        }
        site.setAddress(update.getAddress());
        site.setEmail(update.getEmail());
        site.setPhone(update.getPhone());
        site.getAvailableCars().addAll(availableCars);
        return site;
    }

    public Site transform(SiteDTO s) {
        Site site = new Site();
        site.setAvailableCars(new ArrayList<>());
        return updateSite(site, s);
    }

    //RENTAL

    public Rental transform(RentalDTO rentalDTO){
        Rental rental = new Rental();
        return updateRental(rental, rentalDTO);
    }

    public Rental updateRental(Rental rental, RentalDTO rentalDTO){
        rental.setCar(rentalDTO.getCar());
        rental.setUser(rentalDTO.getUser());
        rental.setFrom(rental.getFrom());
        rental.setTo(rentalDTO.getTo());
        return rental;
    }
}
