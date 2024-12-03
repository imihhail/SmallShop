package com.example.LetsPlay.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.LetsPlay.Model.Users;

public interface UsersRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    
}

