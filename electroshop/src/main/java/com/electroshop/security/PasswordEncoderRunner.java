package com.electroshop.security;

import com.electroshop.model.User;
import com.electroshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class PasswordEncoderRunner {

    @Bean
    CommandLineRunner encodePasswords(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            List<User> users = userRepository.findAll();

            for (User user : users) {
                String plainPassword = user.getPassword();

                // Skip already-encoded passwords
                if (!plainPassword.startsWith("$2a$")) {
                    String encodedPassword = passwordEncoder.encode(plainPassword);
                    user.setPassword(encodedPassword);
                    System.out.println("Encoded password for: " + user.getEmail());
                }
            }

            userRepository.saveAll(users);
            System.out.println("✅ Password encoding complete.");
        };
    }
}

