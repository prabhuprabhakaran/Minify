/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.prabhuprabhakaran.minify.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author Prabhu Prabhakaran
 */
@EnableWebSecurity(debug = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.rest.api.key.header.name}")
    private String principalRequestHeader;

    @Value("${app.rest.api.key.value}")
    private String principalRequestValue;

    public AppSecurityConfig() {
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        , "/static/**"
        http.httpBasic().disable()
                .csrf().disable()
                //Default URL Handlling
                .antMatcher("/").authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs").permitAll()
                .and()
                //Rest URL Handlling
                .antMatcher("/api/**")
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(getAPIKeyFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api").authenticated()
                .antMatchers("/api/**").authenticated()
                .and()
                //APP URL Handlling
                .antMatcher("/app/**")
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .exceptionHandling().accessDeniedPage("/app/denied")
                .and()
                .formLogin()
                .loginPage("/app/login").failureUrl("/app/login")
                .successForwardUrl("/app/home").failureForwardUrl("/app/login")
                .and()
                .logout().logoutUrl("/app/logout").logoutSuccessUrl("/app/login")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/app").permitAll()
                .antMatchers(HttpMethod.HEAD, "/app").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/app/**").permitAll()
                .antMatchers(HttpMethod.HEAD, "/app/**").permitAll()
                .antMatchers("/app/login", "/app/logout", "/app/register", "/app/denied", "/app/error").permitAll()
                .antMatchers("/app/home").authenticated()
                .antMatchers("/app/home/**").authenticated();
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
        System.out.println("CALLING HERE");
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
