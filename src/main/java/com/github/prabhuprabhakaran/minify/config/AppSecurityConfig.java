/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Prabhu Prabhakaran
 */
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.rest.api.key.header.name}")
    private String principalRequestHeader;

    @Value("${app.rest.api.key.value}")
    private String principalRequestValue;

    public AppSecurityConfig() {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        , "/static/**"
        http.antMatcher("/").authorizeRequests()
                .antMatchers("/").permitAll();

        http.antMatcher("/app/**").authorizeRequests()
                .antMatchers("/app/login", "/app/logout", "/app/register", "/app/accessdenied", "/app/error").permitAll()
                .and()
                .formLogin()
                .loginPage("/app/login")
                .failureUrl("/app/login")
                .defaultSuccessUrl("/app/home")
                .successForwardUrl("/app/home")
                .failureForwardUrl("/app/login")
                .and()
                .logout()
                .logoutSuccessUrl("/app/login").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/app/accessdenied");

        http.antMatcher("/api/**")
                .authorizeRequests()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilter(getAPIKeyFilter());

        http.authorizeRequests().anyRequest().authenticated();

//        JwtTokenAuthenticationFilter customFilter = new JwtTokenAuthenticationFilter(jwtTokenProvider);
//
//        http.antMatcher("/api/**")
//                .authorizeRequests()
//                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
//                .and()
//                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .anyRequest().authenticated();
        http.httpBasic().disable();
        http.csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    public APIKeyAuthFilter getAPIKeyFilter() {
        APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
        filter.setAuthenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String principal = (String) authentication.getPrincipal();
                //Get User AuthToken and check Equals
                if (!principalRequestValue.equals(principal)) {
                    throw new BadCredentialsException("The API key was not found or not the expected value.");
                }
                authentication.setAuthenticated(true);
                return authentication;
            }
        });
        return filter;
    }
}
