package com.example.LetsPlay.Model;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.LetsPlay.Repository.UsersRepo;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Users> user = usersRepo.findByUsername(username);
        
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles("USER")
                    .build();
        } else {
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }
    }


}
