package com.example.camp.services;


import com.example.camp.dto.CategoryDTO;
import com.example.camp.dto.ToolDTO;
import com.example.camp.dto.UserDTO;
import com.example.camp.entity.Role;
import com.example.camp.entity.Tools;
import com.example.camp.entity.Users;
import com.example.camp.exception.UserFoundException;
import com.example.camp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
        public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository= userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(java.lang.String username) throws UsernameNotFoundException {
        Users user= userRepository.findByEmail(username);
        if (user==null){
            throw new UsernameNotFoundException("user with email "+username+" not exist");
        }else {
            //log.info("user exist in database");
        }


        return new User(user.getEmail(),user.getPassword(),user.getAuthorities());
    }

    public String getUserEmailById(Integer userId) {
        Optional<Users> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return user.getEmail();
        } else {
            throw new NoSuchElementException("User with ID " + userId + " not found");
        }
    }

    public Users addEmployer(UserDTO userDTO) throws IOException {
        Users userExist = userRepository.findByEmail(userDTO.getEmail());
        if (userExist != null) {
            throw new UserFoundException("Account already exists");
        }

        Users newEmployer = new Users();
        String roleString = userDTO.getRole();

        // Set role based on the roleString value or default to USER if roleString is null
        if (roleString == null || roleString.isEmpty()) {
            newEmployer.setRole(Role.USER); // Default role to USER
        } else if (roleString.equals("EMPLOYER")) {
            newEmployer.setRole(Role.EMPLOYER);
        } else if (roleString.equals("ADMIN")) {
            newEmployer.setRole(Role.ADMIN);
        } else if (roleString.equals("USER")) {
            newEmployer.setRole(Role.USER);
        } else {
            throw new IllegalArgumentException("Invalid role value: " + roleString);
        }

        newEmployer.setEmail(userDTO.getEmail());
        newEmployer.setFirstName(userDTO.getFirstName());
        newEmployer.setLastName(userDTO.getLastName());

        newEmployer.setImageUrl(decodeBase64Image(userDTO.getImageData()));
        newEmployer.setBirthday(userDTO.getBirthday());

        newEmployer.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(newEmployer);
    }

    private UserDTO convertToDTO(Users users) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(users.getUserId());
        userDTO.setFirstName(users.getFirstName());
        userDTO.setLastName(users.getLastName());
        userDTO.setEmail(users.getEmail());
        userDTO.setPassword(users.getPassword());
        userDTO.setBirthday(users.getBirthday());
        userDTO.setPhoneNumber(users.getPhoneNumber());
        // Check if the imageData is not null before encoding
        if (users.getImageUrl() != null) {
            String imageDataString = Base64.getEncoder().encodeToString(users.getImageUrl());
            userDTO.setImageData(imageDataString);
        }
        // Assuming you want to convert the byte array to a Base64 encoded string
        userDTO.setRole(users.getRole().name()); // Convert enum to string

        return userDTO;
    }


    public boolean deleteUser(Integer userId) {
        Optional<Users> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            userRepository.deleteById(userId);
            return true;
        } else {
            return false; // Tool not found
        }
    }



    public UserDTO getUserById(Integer userId) {
        Optional<Users> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return convertToDTO(user);
        } else {
            throw new NoSuchElementException("User with ID " + userId + " not found");
        }
    }


    public List<UserDTO> getAllUsers() {
        List<Users> tools = userRepository.findAll();
        return tools.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private byte[] decodeBase64Image(String base64Image) {
        if (base64Image != null) {
            try {
                // Remove the prefix if present
                String base64Data = base64Image.replaceAll("data:image/jpeg;base64,", "");
                return Base64.getDecoder().decode(base64Data);
            } catch (IllegalArgumentException e) {
                // Log the problematic base64Image value

                throw e; // rethrow the exception or handle it as needed
            }
        }
        return null;
    }
    public Users getUserByemail(String email){
        return userRepository.findByEmail(email);
    }
}
