package com.dudungtak.seproject.config;

import com.dudungtak.seproject.component.JwtUtil;
import com.dudungtak.seproject.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.Filter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //@Value("${jwt.secret}")
    private String secret = "12345678901234567890123456789012";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Filter filter = new JwtAuthenticationFilter(authenticationManager(), jwtUtil());

        http
            .cors().disable()
            .csrf().disable()
            .formLogin().disable()
            .headers().frameOptions().disable()
            .and()
            .addFilter(filter)
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secret);
    }
}
