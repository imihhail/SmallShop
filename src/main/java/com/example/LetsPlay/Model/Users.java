package com.example.LetsPlay.Model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username cannot be empty")
    @Column(unique = true, nullable = false)
    private String username;
    
    private String password;
    private String role;

    @CreationTimestamp
    @Column(name = "time_registered", nullable = false, updatable = false)
    private LocalDateTime timeRegistered;
    
}
