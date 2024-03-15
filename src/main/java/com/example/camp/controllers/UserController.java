package com.example.camp.controllers;


import com.example.camp.dto.ToolDTO;
import com.example.camp.dto.UserDTO;
import com.example.camp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;



    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping(value = "/add")
    public ResponseEntity<?> registerEmployee(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorFields = bindingResult.getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.toList());
                String errorMessage = "Validation errors in fields: " + String.join(", ", errorFields);

                // Log the detailed error message
                logger.error(errorMessage);

                return ResponseEntity.badRequest().body(errorMessage);
            }
            // If it doesn't exist, proceed to register the employee
            userService.addEmployer(userDTO);
            return ResponseEntity.ok("Employee registered successfully.");
        } catch (Exception ex) {
            // Log the exception
            logger.error("An error occurred while registering the employee.", ex);

            // Handle the exception and send an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the employee.");
        }



    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        if (userService.deleteUser(userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Handle not found case
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

}
