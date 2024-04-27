package com.test.Users.controller;

import com.test.Users.dto.UserRequestDTO;
import com.test.Users.model.Users;
import com.test.Users.service.UserService;
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
    public List<UserRequestDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO userRequestDTO){
        if (userRequestDTO.getEmail() == null || userRequestDTO.getFirstName() == null || userRequestDTO.getLastName() == null || userRequestDTO.getBirthDate() == null) {
            return ResponseEntity.badRequest().body("The fields email, first name, last name and date of birth are required.");
        }
        if (!ageChecker.isUserAdult(userRequestDTO.getBirthDate())) {
            return ResponseEntity.badRequest().body("You must be over  " + ageChecker.getAgeLimit() + " years old to register.");
        }
        if (!EmailValidator.isValidEmail(userRequestDTO.getEmail())) {
           throw  new EmailFormatException();
        }
        System.out.println(userRequestDTO.toString());
        Users newUser = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable int userId, @RequestBody UserRequestDTO userRequestDTO){

        Users existingUser = userService.findById(userId);

        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        if (userRequestDTO.getEmail() != null) {
            existingUser.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getFirstName() != null) {
            existingUser.setFirstName(userRequestDTO.getFirstName());
        }
        if (userRequestDTO.getLastName() != null) {
            existingUser.setLastName(userRequestDTO.getLastName());
        }
        if (userRequestDTO.getBirthDate() != null) {
            existingUser.setBirthDate(userRequestDTO.getBirthDate());
        }
        if (userRequestDTO.getAddress() != null) {
            existingUser.setAddress(userRequestDTO.getAddress());
        }
        if (userRequestDTO.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userRequestDTO.getPhoneNumber());
        }

        Users updatedUser = userService.updateUser(existingUser);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateAllInfoForUser(@PathVariable int userId, @RequestBody UserRequestDTO userRequestDTO) {
        Users updatedUser = userService.updateUser(userId, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<?>  deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("The user has been successfully deleted");
    }

    @GetMapping("/searchByBirthDateRange?fromBirthDate=&toBirthDate=")
    public ResponseEntity<?> searchUsersByBirthDateRange(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                         @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

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
