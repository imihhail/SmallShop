package com.example.LetsPlay.Controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.LetsPlay.Model.Products;
import com.example.LetsPlay.Model.Users;
import com.example.LetsPlay.Repository.UsersRepo;
import com.example.LetsPlay.Repository.ProductsRepo;
import com.example.LetsPlay.webToken.JwtService;
import com.example.LetsPlay.webToken.JwtCheck;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MainController {
    @Autowired
    private JwtCheck jwtCheck;

    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

@PostMapping("/registerUser")
public ResponseEntity<?> registerUser(@RequestBody Users newUser) {
    if (newUser.getUsername() == null || newUser.getUsername().isEmpty()) {
        return ResponseEntity.status(400).body(Map.of(
            "message", "Username cannot be empty"
        ));
    }

    if (newUser.getUsername().length() > 20) {
        return ResponseEntity.status(400).body(Map.of(
            "message", "Username cannot exceed 20 characters"
        ));
    }

    if (newUser.getPassword() == null || newUser.getPassword().isEmpty()) {
        return ResponseEntity.status(400).body(Map.of(
            "message", "Password cannot be empty"
        ));
    }

    if (newUser.getPassword().length() > 20) {
        return ResponseEntity.status(400).body(Map.of(
            "message", "Password cannot exceed 20 characters"
        ));
    }

    if (newUser.getRole() != null) {
        return ResponseEntity.status(403).body(Map.of("message", "Role setting not allowed during registration"));
    }

    try {
        newUser.setRole("USER");
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        usersRepo.save(newUser);

        return ResponseEntity.ok(Map.of(
            "message", "Registration successful"
        ));
    } catch (DataIntegrityViolationException e) {
        return ResponseEntity.status(409).body(Map.of(
            "message", "Username is already registered"
        ));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(Map.of(
            "message", "An unexpected error occurred. Please try again later."
        ));
    }
}

@PostMapping("/authenticate")
public ResponseEntity<?> authenticate() {
    if (jwtCheck.authenticated) {
        if ("USER".equals(jwtCheck.role)) {
            var filteredProducts = productsRepo.findByOwnerIn(List.of("No owner", jwtCheck.username));
            return ResponseEntity.ok(Map.of(
                "productsData", filteredProducts,
                "role", jwtCheck.role,
                "username", jwtCheck.username));
        } else if ("ADMIN".equals(jwtCheck.role)) {
            var foundProducts = productsRepo.findAll();
            return ResponseEntity.ok(Map.of(
                "productsData", foundProducts,
                "role", jwtCheck.role,
                "username", jwtCheck.username));
        }
    }
    return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
}

@PostMapping("/insertProduct")
public ResponseEntity<?> insertProduct(@RequestBody Products product) {
    if (jwtCheck.authenticated && "ADMIN".equals(jwtCheck.role)) {
        Products savedProduct = productsRepo.save(product);
        return ResponseEntity.ok(Map.of(
            "productsResponse", "Product inserted",
            "productId", savedProduct.getId()));
    }
    return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
}


@DeleteMapping("/deleteProduct")
public ResponseEntity<?> deleteProduct(@RequestBody Long productId) {
    if (jwtCheck.authenticated && "ADMIN".equals(jwtCheck.role)) {
        productsRepo.deleteById(productId);
        return ResponseEntity.ok(Map.of(
            "productsResponse", "Product deleteed"));
    }
    return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
}

@PutMapping("/updateOwner")
public ResponseEntity<?> updateProduct(@RequestBody Long productId) {
    if (jwtCheck.authenticated && "USER".equals(jwtCheck.role)) {
        Products product = productsRepo.findById(productId).orElse(null);

        if (product != null) {
            product.setOwner(jwtCheck.username);
            productsRepo.save(product);
        } else {
            return null;
        }

        return ResponseEntity.ok(Map.of(
            "productsResponse", "Owner updated"));
    }
    return ResponseEntity.status(403).body(Map.of("message", "Unauthorized"));
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginData) {
        var requestedUsername = usersRepo.findByUsername(loginData.getUsername());
        
        if (requestedUsername.isPresent()) {
            if (passwordEncoder.matches(loginData.getPassword(), requestedUsername.get().getPassword())) {
                String userRole = requestedUsername.get().getRole();
                String token = jwtService.generateToken(loginData.getUsername(), userRole);
                return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", token
                ));
            } else {
                return ResponseEntity.status(401).body("Invalid password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

}
