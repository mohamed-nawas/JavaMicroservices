package com.swivel.ignite.auth.service;

import com.swivel.ignite.auth.entity.Role;
import com.swivel.ignite.auth.exception.AuthException;
import com.swivel.ignite.auth.exception.UserRoleNotFoundException;
import com.swivel.ignite.auth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Role Service
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * This method returns a role from role name
     *
     * @param name role name
     * @return Role/ null
     */
    public Role getRoleByName(String name) {
        try {
            Optional<Role> optionalRole = roleRepository.findByName(name);
            if (!optionalRole.isPresent())
                throw new UserRoleNotFoundException("User role is not found in DB");
            return optionalRole.get();
        } catch (DataAccessException e) {
            throw new AuthException("Failed to get role from DB by name for name: " + name, e);
        }
    }
}
