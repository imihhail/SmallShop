package com.example.LetsPlay.Controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
    System.out.println("registered!");
    System.out.println(newUser);

    if (newUser.getRole() != null) {
        return ResponseEntity.status(403).body(Map.of("message", "Role setting not allowed during registration"));
    }

    newUser.setRole("USER");
    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
    usersRepo.save(newUser);

    return ResponseEntity.ok(Map.of(
        "message", "Registration successful"
    ));
}

@PostMapping("/authenticate")
public ResponseEntity<?> authenticate() {
    if (jwtCheck.authenticated) {
        var allProducts = productsRepo.findByOwner("No owner");
        return ResponseEntity.ok(Map.of(
            "productsData", allProducts,
            "role", jwtCheck.role));
    }
    
    return ResponseEntity.status(403).body(Map.of("message", "UnAuthorized"));
}

@PostMapping("/insertProduct")
public ResponseEntity<?> insertProduct(@RequestBody Products product) {
    System.out.println("product inserted: " + product);
    if (jwtCheck.authenticated && "ADMIN".equals(jwtCheck.role)) {
        productsRepo.save(product);
        return ResponseEntity.ok(Map.of(
            "productsResponse", "Product inserted"));
    }
    return ResponseEntity.status(403).body(Map.of("message", "UnAuthorized"));
}

@PostMapping("/deleteProduct")
public ResponseEntity<?> deleteProduct(@RequestBody Long productId) {
    if (jwtCheck.authenticated && "ADMIN".equals(jwtCheck.role)) {
        productsRepo.deleteById(productId);
        return ResponseEntity.ok(Map.of(
            "productsResponse", "Product deleteed"));
    }
    return ResponseEntity.status(403).body(Map.of("message", "UnAuthorized"));
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
    return ResponseEntity.status(403).body(Map.of("message", "UnAuthorized"));
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users loginData) {
        var requestedUsername = usersRepo.findByUsername(loginData.getUsername());
        
        if (requestedUsername.isPresent()) {
            if (passwordEncoder.matches(loginData.getPassword(), requestedUsername.get().getPassword())) {
                String userRole = requestedUsername.get().getRole();
                System.out.println(productsRepo.findAll());
                // if (!"USER".equals(userRole)) { 
                //     return ResponseEntity.status(403).body(Map.of(
                //         "error", "NO ACCESS!"
                //     ));
                // }

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



    @PostMapping("/security")
    public void testSecurity() {       
        System.out.println("Testing security..");
        System.out.println(productsRepo.findAll());
    }
}
