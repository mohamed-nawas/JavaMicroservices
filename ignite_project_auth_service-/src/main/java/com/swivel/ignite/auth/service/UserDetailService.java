package com.swivel.ignite.auth.service;

import com.swivel.ignite.auth.entity.AuthUserDetail;
import com.swivel.ignite.auth.entity.User;
import com.swivel.ignite.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User Detail Service
 */
@Service("userDetailsService")
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username).isPresent() ?
                userRepository.findByUsername(username) : userRepository.findById(username);

        if (!optionalUser.isPresent())
            throw new UsernameNotFoundException("Invalid username");

        return new AuthUserDetail(optionalUser.get());
    }
}
