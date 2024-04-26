package com.test.Users.controllers;

import com.test.Users.dto.DataRangeDTO;
import com.test.Users.dto.UserDTO;
import com.test.Users.model.Users;
import com.test.Users.services.UserService;
import com.test.Users.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final AgeChecker ageChecker;

    @Autowired
    public UserController(UserService userService, AgeChecker ageChecker) {
        this.userService = userService;
        this.ageChecker = ageChecker;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
        if (userDTO.getEmail() == null || userDTO.getFirstName() == null || userDTO.getLastName() == null || userDTO.getBirthDate() == null) {
            return ResponseEntity.badRequest().body("The fields email, first name, last name and date of birth are required.");
        }
        if (!ageChecker.isUserAdult(userDTO.getBirthDate())) {
            return ResponseEntity.badRequest().body("You must be over  " + ageChecker.getAgeLimit() + " years old to register.");
        }
        if (!EmailValidator.isValidEmail(userDTO.getEmail())) {
           throw  new EmailFormatException();
        }
        System.out.println(userDTO.toString());
        Users newUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable int userId, @RequestBody UserDTO userDTO){

        Users existingUser = userService.findById(userId);

        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getFirstName() != null) {
            existingUser.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            existingUser.setLastName(userDTO.getLastName());
        }
        if (userDTO.getBirthDate() != null) {
            existingUser.setBirthDate(userDTO.getBirthDate());
        }
        if (userDTO.getAddress() != null) {
            existingUser.setAddress(userDTO.getAddress());
        }
        if (userDTO.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        }

        Users updatedUser = userService.updateUser(existingUser);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateAllInfoForUser(@PathVariable int userId, @RequestBody UserDTO userDTO) {
        Users updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/searchByBirthDateRange")
    public ResponseEntity<?> searchUsersByBirthDateRange(@RequestBody DataRangeDTO dataRangeDTO) {

        LocalDate from = dataRangeDTO.getFrom();
        LocalDate to = dataRangeDTO.getTo();

        System.out.println(from +"   " + to);
        if (from.isAfter(to)) {
            return ResponseEntity.badRequest().body("The 'from' date must be before the 'to' date.");
        }

        List<Users> users = userService.findUsersByBirthDateRange(from, to);
        return ResponseEntity.ok(users);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e){
        UserErrorResponse userErrorResponse = new UserErrorResponse(
                "User with this Id wasn`t found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailFormatException.class)
    public ResponseEntity<UserErrorResponse> handleEmailFormatException(EmailFormatException ex) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(
                "Incorrect email format!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
