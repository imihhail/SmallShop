package com.example.LetsPlay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.LetsPlay.Model.Users;
import com.example.LetsPlay.Repository.UsersRepo;

@SpringBootApplication
public class LetsPlayApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(LetsPlayApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UsersRepo usersRepository) {
        return args -> {
            if (usersRepository.findByUsername("Admin").isEmpty()) {
                Users user = new Users();
                user.setUsername("Admin");
                user.setPassword(passwordEncoder.encode("1234"));
                user.setRole("ADMIN");
                usersRepository.save(user);
            };
        };
    };
};
