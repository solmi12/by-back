package com.example.camp;

import com.example.camp.entity.Role;
import com.example.camp.entity.Users;
import com.example.camp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public void run(java.lang.String... args) throws Exception {

        System.out.println("Application started.");
        Users admin=new Users();
        admin.setEmail("admin@admin.com");
        admin.setFirstName("admin");
        admin.setRole(Role.ADMIN);
        admin.setLastName("admin");
        admin.setPassword(passwordEncoder.encode("123"));
       userRepository.save(admin);




        System.out.println("Command-line  executed .");
    }
}