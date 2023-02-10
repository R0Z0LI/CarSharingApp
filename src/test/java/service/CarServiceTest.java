package service;

import banger.Application;
import banger.dto.CarDTO;
import banger.model.Car;
import banger.model.Category;
import banger.repository.CarRepository;
import banger.repository.CategoryRepository;
import banger.service.CarService;
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
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    Transformer transformer;

    @InjectMocks
    private CarService carService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void whenCreateCar_shouldReturnCar() {
        CarDTO carDTO = new CarDTO();
        carDTO.setManufacturer("Skoda");
        Car car = new Car();
        car.setManufacturer("Skoda");

        when(transformer.transform(ArgumentMatchers.any(CarDTO.class))).thenReturn(car);
        when(carRepository.save(ArgumentMatchers.any(Car.class))).thenReturn(car);

        Car createdCar = carService.create(carDTO);

        assertEquals(createdCar.getManufacturer(), car.getManufacturer());
        verify(transformer).transform(carDTO);
        verify(carRepository).save(car);
    }

    @Test
    public void whenCreateCar_shouldReturnNull_ifCarAlreadyExists(){
        CarDTO carDTO = new CarDTO();
        Car car = new Car();

        when(carRepository.existsById(carDTO.getLicensePlate())).thenReturn(true);


        Car response = carService.create(carDTO);

        assertNull(response);
    }

    @Test
    public void whenCreateCar_shouldReturnNull_ifSiteIsWrong(){
        CarDTO carDTO = new CarDTO();

        when(carRepository.existsById(carDTO.getLicensePlate())).thenReturn(false);
        when(transformer.transform(carDTO)).thenReturn(null);

        Car response = carService.create(carDTO);

        assertNull(response);
    }

    @Test
    public void whenGivenId_shouldReturnCar_ifFound(){
        Car car = new Car();
        car.setLicensePlate("BKW189");
        Optional<Car> oCar = Optional.of(car);

        when(carRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oCar);

        Car createdCar = carService.find("BKW189");


        assertEquals(createdCar, car);

        verify(carRepository).findById("BKW189");
    }

    @Test
    public void whenGivenId_shouldReturnNull_ifNotFound(){
        Optional<Car> empty = Optional.empty();


        when(carRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(empty);

        Car createdCar = carService.find("FGB876");

        assertEquals(createdCar, null);

        verify(carRepository).findById("FGB876");
    }

    @Test
    public void shouldReturnAllCars() {
        List<Car> cars = new ArrayList<Car>();
        cars.add(new Car());

        given(carRepository.findAll()).willReturn(cars);
        List<Car> expected = carService.findAll();

        assertEquals(expected, cars);
        verify(carRepository).findAll();
    }

    @Test
    public void whenGivenId_shouldDeleteCar_ifFound() {
        Car car = new Car();
        car.setLicensePlate("VIK123");

        when(carRepository.existsById(car.getLicensePlate())).thenReturn(true);

        carService.deleteById(car.getLicensePlate());

        verify(carRepository).deleteById(car.getLicensePlate());
        verify(carRepository).existsById(car.getLicensePlate());
    }


    @Test
    public void should_throw_null_when_car_doesnt_exist_delete(){

        when(carRepository.existsById(ArgumentMatchers.any(String.class))).thenReturn(false);

        String deleteCar = carService.deleteById("FGB876");

        assertEquals(null, deleteCar);

        verify(carRepository).existsById("FGB876");
    }



    @Test
    public void whenUpdateCar_shouldUpdateCar_ifFound() {
        Car car = new Car();
        CarDTO carDTO = new CarDTO();
        car.setLicensePlate("KSI234");
        carDTO.setLicensePlate("W2S342");
        Category category = new Category();
        category.setName("Alap");
        category.setPricePerHour(500.0);
        category.setId("asd");
        car.setCategory(category);
        carDTO.setCategoryId(category.getId());
        Car transformedCar = new Car();
        transformedCar.setCategory(category);
        transformedCar.setLicensePlate("W2S342");

        when(carRepository.findById(car.getLicensePlate())).thenReturn(Optional.of(car));
        when(carRepository.save(ArgumentMatchers.any(Car.class))).thenReturn(transformedCar);
        when(transformer.updateCar(car, carDTO)).thenReturn(transformedCar);

        Car createdCar = carService.update(car.getLicensePlate(), carDTO);

        assertEquals(createdCar.getLicensePlate(), transformedCar.getLicensePlate());
    }

    @Test
    public void should_throw_null_when_car_doesnt_exist_update(){
        Optional<Car> empty = Optional.empty();

        when(carRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(empty);

        Car updatedCar = carService.update("FGB876", new CarDTO());

        assertEquals(null, updatedCar);

        verify(carRepository).findById("FGB876");
    }

    @Test
    public void shouldThrowNull_whenCategoryDoesNotExist(){
        Car car = new Car();
        CarDTO carDTO = new CarDTO();
        Optional<Car> oCar = Optional.of(car);
        when(carRepository.findById(car.getLicensePlate())).thenReturn(oCar);
        when(transformer.updateCar(car, carDTO)).thenReturn(null);

        Car response = carService.update(car.getLicensePlate(), carDTO);

        assertNull(response);
    }

    @Test
    public void shouldReturnAvailableCars(){
        List<Car> cars = new ArrayList<>();
        cars.add(new Car());

        when(carRepository.findByRenterIsNull()).thenReturn(cars);

        List<Car> responseCars = carService.findAvailable();

        assertEquals(cars, responseCars);
    }
}
