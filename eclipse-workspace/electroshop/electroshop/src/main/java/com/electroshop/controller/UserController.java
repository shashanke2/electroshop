package com.electroshop.controller;

import com.electroshop.dto.UserDTO;
import com.electroshop.model.User;
import com.electroshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPassword(user.getPassword());
        return dto;
    }

    private User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }
    
    //Register new User
    @PostMapping
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
    	userDTO.setRole("USER");
        User savedUser = userService.registerUser(toEntity(userDTO));
        return ResponseEntity.ok(toDTO(savedUser));
    }

    //Get all Users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream().map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    //Get User By Id
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(toDTO(userService.getUserById(id)));
    }

    //Update User By Id
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        User updated = userService.updateUser(id, toEntity(userDTO));
        return ResponseEntity.ok(toDTO(updated));
    }

    //Delete User By Id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}