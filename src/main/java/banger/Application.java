package banger;

import banger.model.Car;
import banger.model.Category;
import banger.model.Site;
import banger.model.User;
import banger.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    RentalRepository rentalRepository;

    public static void main(String[] args) {
        System.out.println("\n\n\nMain started....");
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        User user1 = new User();
        user1.setName("demo");
        user1.setUsername("demo");
        user1.setEmail("demo@banger.com");
        user1.setPhone("+36111111111");
        user1.setLicenseNumber("UU1111");
        user1.setPassword(passwordEncoder.encode("demo"));
        user1.setAdmin(false);
        user1.setEnabled(true);

        User user2 = new User();
        user2.setName("proba");
        user2.setUsername("proba");
        user2.setEmail("proba@banger.com");
        user2.setPhone("+36222222222");
        user2.setLicenseNumber("UU2222");
        user2.setPassword(passwordEncoder.encode("proba"));
        user2.setAdmin(false);
        user2.setEnabled(true);

        User admin = new User();
        admin.setName("admin");
        admin.setUsername("admin");
        admin.setEmail("admin@banger.com");
        admin.setPhone("+36201234567");
        admin.setLicenseNumber("AD1234");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setAdmin(true);
        admin.setEnabled(true);
        
        User root = new User();
        root.setName("root");
        root.setUsername("root");
        root.setEmail("root@banger.com");
        root.setPhone("+36207654321");
        root.setLicenseNumber("RO1234");
        root.setPassword(passwordEncoder.encode("root"));
        root.setAdmin(true);
        root.setEnabled(true);

        userRepository.saveAll(List.of(user1, user2, admin, root));

        Category alap = new Category();
        alap.setName("Alap");
        alap.setPricePerHour(500.0);
        
        Category sport = new Category();
        sport.setName("Sport");
        sport.setPricePerHour(2000.0);
        
        categoryRepository.saveAll(List.of(alap, sport));

        Car skoda = new Car();
        skoda.setManufacturer("Skoda");
        skoda.setModel("Octavia");
        skoda.setLicensePlate("MBR497");
        skoda.setYearProduced(2012);
        skoda.setCategory(alap);
        skoda.setSpace(5);
        
        Car opel = new Car();
        opel.setManufacturer("Opel");
        opel.setModel("Astra");
        opel.setLicensePlate("JEA092");
        opel.setYearProduced(2004);
        opel.setCategory(alap);
        opel.setSpace(5);
        
        Car ferrari = new Car();
        ferrari.setManufacturer("Ferrari");
        ferrari.setModel("F50");
        ferrari.setLicensePlate("FER500");
        ferrari.setYearProduced(2020);
        ferrari.setCategory(sport);
        ferrari.setSpace(2);

        Car rr = new Car();
        rr.setManufacturer("Rolls Royce");
        rr.setModel("Phantom");
        rr.setLicensePlate("RRR420");
        rr.setYearProduced(2022);
        rr.setCategory(sport);
        rr.setSpace(5);

        Site bme = new Site();
        bme.setAddress("1111 Budapest, Műegyetem rkp. 3.");
        bme.setEmail("bangerbme@banger.com");
        bme.setPhone("+36203374212");
        ArrayList<Car> bmeCars = new ArrayList<>();
        bmeCars.add(skoda);
        bmeCars.add(ferrari);
        bme.setAvailableCars(bmeCars);
        skoda.setSite(bme);
        ferrari.setSite(bme);

        Site parlament = new Site();
        parlament.setAddress("1051 Budapest, Kossuth tér 1.");
        parlament.setEmail("bangerparlament@banger.com");
        parlament.setPhone("+36203822312");
        ArrayList<Car> pCars = new ArrayList<>();
        pCars.add(opel);
        pCars.add(rr);
        opel.setSite(parlament);
        rr.setSite(parlament);
        parlament.setAvailableCars(pCars);

        siteRepository.saveAll(List.of(bme, parlament));
    }
}
