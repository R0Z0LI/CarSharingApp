package banger.controller;

import banger.dto.LoginDTO;
import banger.dto.UserDTO;
import banger.model.User;
import banger.repository.UserRepository;
import banger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService service;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<Principal> userData(Principal principal) {
        return new ResponseEntity<>(principal, HttpStatus.OK);
    }

    @GetMapping("/confirm/{id}")
    public String confirmRegistration(@PathVariable("id") String userId){
        Optional<User> u = userRepository.findById(userId);
        if(u.isEmpty()) return "Nincs ilyen felhasználó!";
        u.get().setEnabled(true);
        userRepository.save(u.get());
        return "Sikeres megerősítés!";
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@Valid @RequestBody LoginDTO login){
        User user = service.loginUser(login);
        if(user == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserDTO user){
        return service.registerUser(user);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(name="id") String userId, @Valid @RequestBody UserDTO update){
        User updated = service.update(userId, update);
        if(updated == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> find(@PathVariable("id") String userID){
        User user = service.find(userID);
        if(user == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String userID){
        String message = service.delete(userID);
        if(message == null) return ResponseEntity.badRequest().body("Nincs ilyen autó!");
        return ResponseEntity.ok(message);
    }
}