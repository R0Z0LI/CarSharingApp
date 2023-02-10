package service;


import banger.Application;
import banger.dto.RentalDTO;
import banger.model.Car;
import banger.model.Rental;
import banger.model.Site;
import banger.model.User;
import banger.repository.CarRepository;
import banger.repository.RentalRepository;
import banger.repository.SiteRepository;
import banger.repository.UserRepository;
import banger.service.RentalService;
import banger.service.transformer.Transformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = Application.class)
public class RentalServiceTest {
    @Mock
    Transformer transformer;

    @InjectMocks
    private RentalService rentalService;

    @Mock
    RentalRepository rentalRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CarRepository carRepository;

    @Mock
    private SiteRepository siteRepository;

    @Test
    public void whenAddRental_shouldReturnRental(){
        User user = new User();
        user.setId("asd");
        Car car = new Car();
        car.setLicensePlate("ABC123");
        Site site = new Site();
        site.setId("asd");
        site.getAvailableCars().add(car);
        car.setSite(site);
        Optional<User> oRenter = Optional.of(user);
        Optional<Car> oCar = Optional.of(car);
        Rental rental = new Rental();
        rental.setCar(car);

        when(userRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oRenter);
        when(carRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oCar);
        when(carRepository.save(ArgumentMatchers.any(Car.class))).thenReturn(car);
        when(rentalRepository.save(ArgumentMatchers.any(Rental.class))).thenReturn(rental);

        Rental createdRental = rentalService.addRental(car.getLicensePlate(), user.getId());

        assertEquals(car, createdRental.getCar());

        verify(userRepository).findById(user.getId());
        verify(carRepository).findById(car.getLicensePlate());
        verify(carRepository).save(car);
    }

    @Test
    public void whenAddRental_shouldReturnNull_ifUserDoesNotExist(){
        Car car = new Car();
        User user = new User();
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Rental response = rentalService.addRental(car.getLicensePlate(), user.getId());

        assertNull(response);
    }

    @Test
    public void whenAddRental_shouldReturnNull_ifCarDoesNotExist(){
        Car car = new Car();
        User user = new User();
        Optional<User> oUser = Optional.of(user);
        when(userRepository.findById(user.getId())).thenReturn(oUser);
        when(carRepository.findById(car.getLicensePlate())).thenReturn(Optional.empty());

        Rental response = rentalService.addRental(car.getLicensePlate(), user.getId());

        assertNull(response);
    }

    @Test
    public void whenAddRental_shouldReturnNull_ifSomeoneAlredyRentigTheCar(){
        User user = new User();
        Car car = new Car();
        car.setRenter(user);
        Optional<Car> oCar = Optional.of(car);
        Optional<User> oUser = Optional.of(user);
        Rental rental = new Rental();
        rental.setCar(car);

        when(userRepository.findById(user.getId())).thenReturn(oUser);
        when(carRepository.findById(car.getLicensePlate())).thenReturn(oCar);

        Rental response = rentalService.addRental(car.getLicensePlate(), user.getId());

        assertNull(response);
    }

    @Test
    public void whenGivenId_shouldReturnRental_ifFound(){
        Rental rental = new Rental();
        rental.setId("abc123");
        Optional<Rental> oRental = Optional.of(rental);

        when(rentalRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oRental);

        Rental foundRental = rentalService.find(rental.getId());

        assertEquals(rental, foundRental);

        verify(rentalRepository).findById(rental.getId());
    }

    @Test
    public void whenGivenId_shouldReturnNull_ifNotFound(){
        Optional<Rental> empty = Optional.empty();
        Rental rental = null;

        when(rentalRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(empty);

        Rental createdCategory = rentalService.find("FGB876");


        assertEquals(rental, createdCategory);

        verify(rentalRepository).findById("FGB876");
    }

    @Test
    public void shouldReturnAllRentals() {
        List<Rental> rentals = new ArrayList<Rental>();
        rentals.add(new Rental());

        given(rentalRepository.findAll()).willReturn(rentals);

        List<Rental> actual = rentalService.findAll();

        assertEquals(rentals, actual);
        verify(rentalRepository).findAll();
    }

    @Test
    public void when_given_userId_should_return_all_rentals(){
        User user = new User();
        user.setId("asd");
        Optional<User> oUser = Optional.of(user);

        List<Rental> rentals = new ArrayList<Rental>();
        rentals.add(new Rental());

        when(userRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oUser);
        when(rentalRepository.getByUserAndToIsNotNull(user)).thenReturn(rentals);

        List<Rental> created = rentalService.findInactiveByUserID(user.getId());


        assertEquals(rentals ,created);

        verify(userRepository).findById(user.getId());
        verify(rentalRepository).getByUserAndToIsNotNull(user);
    }

    @Test
    public void whenFindInactiveByUserId_shouldReturnNull_ifUserDoesNotExist(){
        User user = new User();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        List<Rental> response = rentalService.findInactiveByUserID(user.getId());

        assertNull(response);
    }

    @Test
    public void whenFindActiveByUserId_shouldReturnRentals_ifUserExists(){
        User user = new User();
        List<Rental> rentals = new ArrayList<>();
        rentals.add(new Rental());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(rentalRepository.getByUserAndToIsNull(user)).thenReturn(rentals);

        List<Rental> response = rentalService.findActiveByUserID(user.getId());

        assertEquals(rentals, response);
    }

    @Test
    public void whenFindActiveByUserId_shouldReturnNull_ifUserDoesNotExist(){
        User user = new User();

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        List<Rental> response = rentalService.findActiveByUserID(user.getId());

        assertNull(response);
    }

    @Test
    public void whenGivenUserId_shouldReturnNull_ifNotFound(){
        Optional<Rental> empty = Optional.empty();
        Rental rental = null;

        when(rentalRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(empty);

        Rental updatedCategory = rentalService.find("asd");


        assertEquals(rental, updatedCategory);

        verify(rentalRepository).findById("asd");
    }

    @Test
    public void whenGivenId_shouldEndRental_ifFound() {
        Car car = new Car();
        car.setLicensePlate("ABC123");
        Site site = new Site();
        site.setId("qwe");
        Optional<Site> oSite = Optional.of(site);
        Optional<Car> oCar = Optional.of(car);
        Rental rental = new Rental();
        rental.setId("asd");
        rental.setCar(car);
        Optional<Rental> oRental = Optional.of(rental);

        when(rentalRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oRental);
        when(siteRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oSite);
        when(carRepository.save(ArgumentMatchers.any(Car.class))).thenReturn(car);
        when(rentalRepository.save(ArgumentMatchers.any(Rental.class))).thenReturn(rental);
        when(siteRepository.save(ArgumentMatchers.any(Site.class))).thenReturn(site);

        rentalService.endRental(rental.getId(), site.getId());

        verify(siteRepository).findById(site.getId());
        verify(carRepository).save(car);
        verify(rentalRepository).save(rental);
        verify(siteRepository).save(site);
    }

    @Test
    public void whenEndingRental_shouldReturnNull_ifRentalDoesNotExist(){
        Rental rental = new Rental();
        Site site = new Site();

        when(rentalRepository.findById(rental.getId())).thenReturn(Optional.empty());

        Rental response = rentalService.endRental(rental.getId(), site.getId());

        assertNull(response);
    }

    @Test
    public void whenEndingRental_shouldReturnNull_ifSiteDoesNotExist(){
        Rental rental = new Rental();
        Site site = new Site();

        when(rentalRepository.findById(rental.getId())).thenReturn(Optional.of(rental));
        when(siteRepository.findById(site.getId())).thenReturn(Optional.empty());

        Rental response = rentalService.endRental(rental.getId(), site.getId());

        assertNull(response);
    }

    @Test
    public void whenUpdateCategory_shouldUpdateRental_ifFound(){
        Rental rental = new Rental();
        RentalDTO rentalDTO = new RentalDTO();
        User user = new User();
        User userDTO = new User();
        Car car = new Car();
        car.setLicensePlate("POK123");
        rental.setId("asd");
        rental.setUser(user);
        rentalDTO.setUser(userDTO);
        Rental transformedRentalDTO = new Rental();
        transformedRentalDTO.setUser(userDTO);

        when(rentalRepository.findById(car.getLicensePlate())).thenReturn(Optional.of(rental));
        when(rentalRepository.save(ArgumentMatchers.any(Rental.class))).thenReturn(transformedRentalDTO);
        when(transformer.updateRental(rental, rentalDTO)).thenReturn(transformedRentalDTO);



        Rental createdCategory = rentalService.update(car.getLicensePlate(), rentalDTO);

        assertEquals(transformedRentalDTO, createdCategory);

        verify(rentalRepository).findById(car.getLicensePlate());
        verify(rentalRepository).save(transformedRentalDTO);
    }

    @Test
    public void should_throw_null_when_rental_doesnt_exist_inUpdate(){
        Optional<Rental> empty = Optional.empty();
        when(rentalRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(empty);

        Rental updatedRental = rentalService.update("asd", new RentalDTO());


        assertEquals(null, updatedRental);

        verify(rentalRepository).findById("asd");
    }
}

