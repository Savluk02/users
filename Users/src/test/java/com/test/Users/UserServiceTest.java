package com.test.Users;

import com.test.Users.model.Users;
import com.test.Users.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testFindById() {
        // Arrange
        int userId = 1;

        // Act
        Users user = userService.findById(userId);

        // Assert
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
    }
}
