package com.swivel.ignite.auth.service;

import com.swivel.ignite.auth.entity.User;
import com.swivel.ignite.auth.exception.AuthException;
import com.swivel.ignite.auth.exception.UserAlreadyExistsException;
import com.swivel.ignite.auth.exception.UserNotFoundException;
import com.swivel.ignite.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * User Service
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method creates a User in the database
     *
     * @param user user
     */
    public void createUser(User user) {
        try {
            if (isUserExists(user.getId()))
                throw new UserAlreadyExistsException("User already exists in DB");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new AuthException("Failed to save user to DB for user id: {}" + user.getId(), e);
        }
    }

    private boolean isUserExists(String id) {
        try {
            return userRepository.findById(id).isPresent();
        } catch (DataAccessException e) {
            throw new AuthException("Failed to check for user existence in DB for id: " + id, e);
        }
    }

    @Transactional
    public void deleteUser(String username) {
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (!optionalUser.isPresent())
                throw new UserNotFoundException("User is not found in auth db for username: " + username);
            User user = optionalUser.get();
            userRepository.delete(user);
        } catch (DataAccessException e) {
            throw new AuthException("Failed to delete user from auth db for username: " + username, e);
        }
    }
}
