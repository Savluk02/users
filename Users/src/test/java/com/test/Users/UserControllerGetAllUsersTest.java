package com.test.Users;

import com.test.Users.controllers.UserController;
import com.test.Users.dto.UserDTO;
import com.test.Users.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerGetAllUsersTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        // Arrange
        List<UserDTO> mockUsers = new ArrayList<>();
        mockUsers.add(new UserDTO("john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St, City", "123-456-7890"));
        mockUsers.add(new UserDTO("jane.smith@example.com", "Jane", "Smith", LocalDate.of(1995, 5, 15), "456 Elm St, Town", "456-789-0123"));

        Mockito.when(userService.getAllUsers()).thenReturn(mockUsers);

        // Act
        List<UserDTO> result = userController.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), result.get(0).getBirthDate());
        assertEquals("123 Main St, City", result.get(0).getAddress());
        assertEquals("123-456-7890", result.get(0).getPhoneNumber());

        assertEquals("jane.smith@example.com", result.get(1).getEmail());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
        assertEquals(LocalDate.of(1995, 5, 15), result.get(1).getBirthDate());
        assertEquals("456 Elm St, Town", result.get(1).getAddress());
        assertEquals("456-789-0123", result.get(1).getPhoneNumber());
    }
}
