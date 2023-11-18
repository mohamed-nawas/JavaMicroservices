package com.swivel.ignite.auth.controllers;

import com.swivel.ignite.auth.dto.request.UserRegistrationRequestDto;
import com.swivel.ignite.auth.dto.response.UserResponseDto;
import com.swivel.ignite.auth.entity.Role;
import com.swivel.ignite.auth.entity.User;
import com.swivel.ignite.auth.enums.ErrorResponseStatusType;
import com.swivel.ignite.auth.enums.SuccessResponseStatusType;
import com.swivel.ignite.auth.service.RoleService;
import com.swivel.ignite.auth.service.UserService;
import com.swivel.ignite.auth.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 */
@RestController
@RequestMapping("api/v1/auth/users")
@Slf4j
public class UserController extends Controller {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * This method creates a User
     *
     * @param requestDto user registration request dto
     * @return success(user response)/ error response
     */
    @PostMapping(path = "/register", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserRegistrationRequestDto requestDto) {
        if (!requestDto.isRequiredAvailable()) {
            log.error("Required fields missing in user registration request DTO for creating user");
            return getBadRequestResponse(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
        }
        Role userRole = roleService.getRoleByName(requestDto.getRoleType().name());
        requestDto.setRole(userRole);
        User user = new User(requestDto);
        userService.createUser(user);
        UserResponseDto responseDto = new UserResponseDto(user);
        log.debug("Created user {}", responseDto.toLogJson());
        return getSuccessResponse(SuccessResponseStatusType.CREATE_USER, responseDto);
    }

    @DeleteMapping(path = "/delete/{username}", produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable(name = "username") String username) {
        userService.deleteUser(username);
        log.debug("Successfully deleted the user of username: {}", username);
        return getSuccessResponse(SuccessResponseStatusType.DELETE_USER, null);
    }
}
