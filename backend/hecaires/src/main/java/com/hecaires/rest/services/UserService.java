package com.hecaires.rest.services;

import com.hecaires.rest.exceptions.ResourceNotFoundException;
import com.hecaires.rest.models.User;
import com.hecaires.rest.repositories.UserRepository;
import com.hecaires.rest.security.CurrentUser;
import com.hecaires.rest.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserRepository userRepository;

    public User getUserById(String userId) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new ResourceNotFoundException("User not found!");
        }
    }

    public CurrentUser getAuthCurrentUser() {
        return userDetailsService.getCurrentUser();
    }

    public User getCurrentUser() throws ResourceNotFoundException {
        CurrentUser currentUser = userDetailsService.getCurrentUser();

        return getUserById(currentUser.getId());
    }
}