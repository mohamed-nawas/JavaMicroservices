package com.swivel.ignite.auth.repository;

import com.swivel.ignite.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * This method finds a user by username
     *
     * @param userName username
     * @return User/ null
     */
    Optional<User> findByUsername(String userName);
}
