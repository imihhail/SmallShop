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
        authenticated = false;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        try {
            username = jwtService.extractUsername(jwt);
            
            if (username != null) {
                Optional<Users> user = usersRepo.findByUsername(username);
    
                if (user.isPresent() && jwtService.isTokenValid(jwt)) {
                    role = user.get().getRole();
                    authenticated = true;
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid token or user not found");
                    return;
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Session expired, login again");
            return;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed");
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Something went wrong, try again later");
            return;
        }
        filterChain.doFilter(request, response);
    }
}