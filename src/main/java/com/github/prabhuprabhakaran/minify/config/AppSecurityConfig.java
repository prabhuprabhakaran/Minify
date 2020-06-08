package com.github.prabhuprabhakaran.minify.config;

import com.github.prabhuprabhakaran.minify.controller.service.LoginService;
import com.github.prabhuprabhakaran.minify.entity.Users;
import com.github.prabhuprabhakaran.minify.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Prabhu Prabhakaran
 */
@EnableWebSecurity(debug = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService loginService;

    public AppSecurityConfig() {
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**", "/favicon.ico");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService)
                .passwordEncoder(passwordEncoder());
    }

    @Order(1)
    @Configuration
    public static class RestConfiguration extends WebSecurityConfigurerAdapter {

        @Value("${app.rest.api.key.header.name}")
        private String principalRequestHeader;

        @Autowired
        UserRepository userRepository;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                    .cors().and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/api/denied").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(getAPIKeyFilter())
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        public APIKeyAuthFilter getAPIKeyFilter() {
            APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
            filter.setAuthenticationManager(new AuthenticationManager() {
                @Override
                public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                    String principal = (String) authentication.getPrincipal();
                    Optional<Users> lOptionaUser = userRepository.findByToken(principal);
                    if (lOptionaUser.isPresent()) {
                        if (lOptionaUser.get().getToken().equals(principal)) {
                            authentication.setAuthenticated(true);
                            return authentication;
                        }
                    }
                    throw new BadCredentialsException("The API key was not found or not the expected value.");
                }
            });
            return filter;
        }
    }

    @Order(2)
    @Configuration
    public static class WebConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/**")
                    .csrf()
                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/**/favicon.ico", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.xlsx", "/fonts/**").permitAll()
                    .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs").permitAll()
                    .antMatchers("/app/login", "/app/logout", "/app/register", "/*").permitAll()
                    .antMatchers("/app/home").authenticated()
                    .antMatchers("/app/home/**").authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/app/login").failureUrl("/app/login?error=loginError")
                    .successForwardUrl("/app/home")
                    .permitAll()
                    .defaultSuccessUrl("/app/home", true)
                    .and()
                    .logout().logoutUrl("/app/logout").logoutSuccessUrl("/app/login");
        }

    }
}
