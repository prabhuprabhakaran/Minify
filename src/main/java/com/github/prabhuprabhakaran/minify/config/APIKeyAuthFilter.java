/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.config;

import com.github.prabhuprabhakaran.minify.entity.Users;
import com.github.prabhuprabhakaran.minify.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Prabhu Prabhakaran
 */
@Component
public class APIKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Value("${app.rest.api.key.header.name}")
    private String principalRequestHeader;
    @Autowired
    UserRepository userRepository;

    public APIKeyAuthFilter() {
        this.setAuthenticationManager(
                new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                UserDetails lUsers = (UserDetails) authentication.getPrincipal();
                if (lUsers != null) {
                    authentication.setAuthenticated(true);
                    return authentication;
                }
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
        });
    }

    @Override

    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String header = request.getHeader(principalRequestHeader);
        try {
            UUID.fromString(header);
            Optional<Users> lOptionaUser = userRepository.findByToken(header);
            if (lOptionaUser.isPresent()) {
                return lOptionaUser.get();
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }

}
