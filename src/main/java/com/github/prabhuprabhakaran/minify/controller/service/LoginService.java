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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
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
            users.setUsername(username.toLowerCase());
            users.setToken(UUID.randomUUID().toString());
            users.setPassword(passwordEncoder.encode(password));
            userRepository.save(users);
            lReturn = true;
        } else if (findByUsername.isPresent()) {
            Users users = findByUsername.get();
            try {
                passwordEncoder.upgradeEncoding(users.getPassword());
            } catch (IllegalArgumentException e) {
                users.setPassword(passwordEncoder.encode(password));
                userRepository.save(users);
                lReturn = true;
            }
        }
        return lReturn;
    }

    public boolean authenticateOAuthUser(String username, String id) {
        System.out.println(username);
        boolean lReturn = false;
        Optional<Users> findByUsername = userRepository.findByUsername(username.toLowerCase());
        if (findByUsername.isPresent()) {
            Users lUser = findByUsername.get();
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(lUser, "", lUser.getAuthorities()));
            lReturn = true;
        } else {
            boolean registerNewUser = registerNewUser(username, id);
            if (registerNewUser) {
                lReturn = true;
            }
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
