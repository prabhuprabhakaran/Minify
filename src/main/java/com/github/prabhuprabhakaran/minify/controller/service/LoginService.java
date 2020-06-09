/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.controller.service;

import com.github.prabhuprabhakaran.minify.entity.Users;
import com.github.prabhuprabhakaran.minify.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Service
public class LoginService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean registerNewUser(String username, String password) {
        boolean lReturn = false;
        Optional<Users> findByUsername = userRepository.findByUsername(username.toLowerCase());
        if (!findByUsername.isPresent()) {
            Users users = new Users();
            users.setUsername(username);
            users.setToken(UUID.randomUUID().toString());
            users.setPassword(passwordEncoder.encode(password));
            userRepository.save(users);
            lReturn = true;
        }
        return lReturn;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> findByUsername = userRepository.findByUsername(username.toLowerCase());
        if (findByUsername.isPresent()) {
            return findByUsername.get();
        }
        return null;
    }

    public Users getUser(String username) throws UsernameNotFoundException {
        Optional<Users> findByUsername = userRepository.findByUsername(username.toLowerCase());
        if (findByUsername.isPresent()) {
            return findByUsername.get();
        }
        return null;
    }

}
