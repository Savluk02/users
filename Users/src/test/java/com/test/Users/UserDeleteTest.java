package com.test.Users;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import com.test.Users.controller.UserController;
import com.test.Users.service.UserService;
import com.test.Users.util.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDeleteTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testDeleteUser_Success() {
        int userId = 1;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<?> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User deleted successfully", responseEntity.getBody());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    public void testDeleteUser_NotFound() {
        int userId = 1;
        doThrow(new UserNotFoundException()).when(userService).deleteUser(userId);

        ResponseEntity<?> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
        verify(userService, times(1)).deleteUser(userId);
    }
}
