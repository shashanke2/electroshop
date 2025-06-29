package com.electroshop.service;

import com.electroshop.model.User;
import java.util.List;

public interface UserService {

    User registerUser(User user); 

    List<User> getAllUsers();

    User getUserById(Long id);
    
    User getUserByEmail(String email);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);
}
