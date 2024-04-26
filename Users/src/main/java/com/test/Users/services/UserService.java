package com.test.Users.services;

import com.test.Users.dto.UserDTO;
import com.test.Users.model.Users;
import com.test.Users.repositories.UserRepository;
import com.test.Users.util.UserNotFoundException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Users createUser(UserDTO userDTO){

        Users users = new Users(userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName(),userDTO.getBirthDate(), userDTO.getAddress(), userDTO.getPhoneNumber());
        return userRepository.save(users);
    }

    public List<Users> findUsersByBirthDateRange(LocalDate from, LocalDate to) {
        return userRepository.findByBirthDateBetween(from, to);
    }

    public Users findById(int userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public Users updateUser(Users users){
        return userRepository.save(users);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDto(Users user) {
        return new UserDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthDate(),
                user.getAddress(),
                user.getPhoneNumber()
        );
    }
    @Transactional
    public Users updateUser(int userId, UserDTO userDTO) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Оновлення всіх полів користувача
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setBirthDate(userDTO.getBirthDate());
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
