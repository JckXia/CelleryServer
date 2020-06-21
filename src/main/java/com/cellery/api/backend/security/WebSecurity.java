package com.cellery.api.backend.security;

import com.cellery.api.backend.shared.Util.JwtUtil;
import com.cellery.api.backend.ui.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Environment environment;
    private UsersService usersService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtUtil jwtUtil;

    @Autowired
    public WebSecurity(Environment environment, UsersService userService, BCryptPasswordEncoder bCryptPasswordEncoder,
                       JwtUtil jwtUtil) {
        this.environment = environment;
        this.usersService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and();
        http.csrf().disable();
        http.headers().frameOptions().disable();
        //http.authorizeRequests().antMatchers("/**").permitAll();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users/create").permitAll()
                .antMatchers(HttpMethod.POST, "/users/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(getAuthorizationFilter())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // http.authorizeRequests().antMatchers("/**").hasIpAddress( "127.0.0.1").and().addFilter(getAuthenticationFilter());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        //We will enable all traffic for now.
        ArrayList<String> header = new ArrayList<>();
        header.add("token");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setExposedHeaders(header);
        source.registerCorsConfiguration("/**", config.applyPermitDefaultValues());
        return source;
    }

    private JwtAuthenticationFilter getAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(environment, usersService, authenticationManager(), jwtUtil);
        authenticationFilter.setFilterProcessesUrl("/users/login");
        return authenticationFilter;
    }

    private JwtAuthorizationFilter getAuthorizationFilter() throws Exception {
        JwtAuthorizationFilter authorizationFilter = new JwtAuthorizationFilter(environment, authenticationManager(), jwtUtil);
        return authorizationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
    }
}
