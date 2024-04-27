package com.test.Users;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.test.Users.controller.UserController;
import com.test.Users.dto.UserRequestDTO;
import com.test.Users.model.Users;
import com.test.Users.service.UserService;
import com.test.Users.util.AgeChecker;
import com.test.Users.util.EmailValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerCreateUserTest {

    @Mock
    private UserService userService;

    @Mock
    private AgeChecker ageChecker;

    @Mock
    private EmailValidator emailValidator;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUser_ValidUser_ReturnsCreated() {

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("john.doe@example.com");
        userRequestDTO.setFirstName("John");
        userRequestDTO.setLastName("Doe");
        userRequestDTO.setBirthDate(LocalDate.ofEpochDay(1990 - 1 - 1));

        Users newUser = new Users();
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(newUser);
        when(ageChecker.isUserAdult(any(LocalDate.class))).thenReturn(true);
        when(EmailValidator.isValidEmail(anyString())).thenReturn(true);

        // Act
        ResponseEntity<?> response = userController.createUser(userRequestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newUser, response.getBody());
    }

    @Test
    public void testCreateUser_InvalidEmailFormat_ReturnsBadRequest() {

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("invalid-email-format");

        ResponseEntity<?> response = userController.createUser(userRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Incorrect email format!", response.getBody());
    }
}