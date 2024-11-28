package com.example.LetsPlay.webToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.LetsPlay.Model.Users;
import com.example.LetsPlay.Repository.UsersRepo;
import java.io.IOException;
import java.util.Optional;


@Configuration
public class JwtCheck extends OncePerRequestFilter {
    public boolean authenticated;
    public String role;
    public String username;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UsersRepo usersRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        
        if (username != null ) {
            Optional<Users> user = usersRepo.findByUsername(username);

            if (user.isPresent() && jwtService.isTokenValid(jwt)) {
                role = user.get().getRole();
                authenticated = true;
            } else {
                System.out.println("user is not present or invalid token!");
            }
        }
        filterChain.doFilter(request, response);
    }
}