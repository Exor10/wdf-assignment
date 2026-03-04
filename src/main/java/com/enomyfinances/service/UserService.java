package com.enomyfinances.service;

import com.enomyfinances.dto.RegistrationRequest;
import com.enomyfinances.entity.Role;
import com.enomyfinances.entity.RoleName;
import com.enomyfinances.entity.User;
import com.enomyfinances.exception.ValidationException;
import com.enomyfinances.repository.RoleRepository;
import com.enomyfinances.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerClient(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Username already in use");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already in use");
        }

        Role role = roleRepository.findByName(RoleName.CLIENT)
                .orElseThrow(() -> new ValidationException("Default role CLIENT missing"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
