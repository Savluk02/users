package com.test.Users.service;

import com.test.Users.dto.UserRequestDTO;
import com.test.Users.model.Users;
import com.test.Users.repository.UserRepository;
import com.test.Users.util.UserNotFoundException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Users createUser(UserRequestDTO userRequestDTO){

        Users users = new Users(userRequestDTO.getEmail(), userRequestDTO.getFirstName(), userRequestDTO.getLastName(), userRequestDTO.getBirthDate(), userRequestDTO.getAddress(), userRequestDTO.getPhoneNumber());
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
    public List<UserRequestDTO> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserRequestDTO convertToDto(Users user) {
        return new UserRequestDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthDate(),
                user.getAddress(),
                user.getPhoneNumber()
        );
    }
    @Transactional
    public Users updateUser(int userId, UserRequestDTO userRequestDTO) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setFirstName(userRequestDTO.getFirstName());
        existingUser.setLastName(userRequestDTO.getLastName());
        existingUser.setBirthDate(userRequestDTO.getBirthDate());
        existingUser.setAddress(userRequestDTO.getAddress());
        existingUser.setPhoneNumber(userRequestDTO.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
