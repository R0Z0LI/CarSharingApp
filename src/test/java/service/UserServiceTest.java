package service;


import banger.Application;
import banger.dto.LoginDTO;
import banger.dto.UserDTO;
import banger.model.User;
import banger.repository.UserRepository;
import banger.service.UserService;
import banger.service.transformer.Transformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

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
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Transformer transformer;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    MailSender mailSender;


    @Test
    public void whenGivenId_shouldReturnUser_ifFound(){
        User user = new User();
        user.setId("asd");
        user.setName("Zoli");

        Optional<User> oUser = Optional.of(user);

        when(userRepository.findById(ArgumentMatchers.any(String.class))).thenReturn(oUser);

        User foundUser = userService.find("asd");

        assertEquals(user, foundUser);

        verify(userRepository).findById(user.getId());
    }

    @Test
    public void whenLoginUser_shouldReturnNull_ifUserDoesNotExist(){
        User user = new User();
        LoginDTO loginDTO = new LoginDTO();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        User response = userService.loginUser(loginDTO);

        assertNull(response);
    }



    @Test
    public void whenGivenLoginDTO_shouldLogInTheUser_ifFound(){
        User user = new User();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("Soma");
        user.setName("Soma");

        Optional<User> oUser = Optional.of(user);

        when(userRepository.findByUsername(loginDTO.getUsername())).thenReturn(oUser);

        User created = userService.loginUser(loginDTO);

        assertEquals(user, created);
    }

    /*@Test
    public void whenGivenSite_notifySiteDeletation(){
        Site site = new Site();
        List<User> users = new ArrayList<User>();
        users.add(new User());
        JavaMailSender javaMailSender = new JavaMailSenderImpl();
        MimeMessage message = javaMailSender.createMimeMessage();


        when(userRepository.findAll()).thenReturn(users);
    }*/

    @Test
    public void shouldReturnAllCategories() {
        List<User> users = new ArrayList<User>();
        users.add(new User());

        given(userRepository.findAll()).willReturn(users);


        List<User> expected = userService.findAll();

        assertEquals(expected, users);
        verify(userRepository).findAll();
    }

    @Test
    public void whenGivenId_shouldDeleteCategory_ifFound() {
        User user = new User();
        user.setName("Soma");

        when(userRepository.existsById(user.getId())).thenReturn(true);

        String responseEntity = userService.delete(user.getId());

        assertEquals("Sikeres felhasználó törlés!", responseEntity);

        verify(userRepository).deleteById(user.getId());
        verify(userRepository).existsById(user.getId());
    }

    @Test
    public void whenUpdateUser_shouldUpdateUser_ifFound(){
        User user = new User();
        user.setId("asd");
        user.setName("Dani");

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Zoli");

        User transformedUserDTO = new User();
        transformedUserDTO.setName("Zoli");
        transformedUserDTO.setId("asd");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(transformer.updateUser(user, userDTO)).thenReturn(transformedUserDTO);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(transformedUserDTO);

        User updatedUser = userService.update(user.getId(), userDTO);

        assertEquals(updatedUser.getName(), transformedUserDTO.getName());

        verify(userRepository).findById(user.getId());
        verify(userRepository).save(transformedUserDTO);
    }

    @Test
    public void whenUpdateUser_shouldReturnNull_ifUserDoesNotExist(){
        UserDTO userDTO = new UserDTO();
        String id = "asd";

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        User user = userService.update(id, userDTO);

        assertNull(user);
    }
}
