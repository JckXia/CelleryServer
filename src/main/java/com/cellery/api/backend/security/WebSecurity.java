package com.cellery.api.backend.security;

import com.cellery.api.backend.ui.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private Environment environment;
    private UsersService usersService;

    @Autowired
    public WebSecurity(Environment environment, UsersService userService){
        this.environment=environment;
        this.usersService=userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers("/**").permitAll();
       // http.authorizeRequests().antMatchers("/**").hasIpAddress( "127.0.0.1").and().addFilter(getAuthenticationFilter());
    }

    private JwtAuthenticationFilter getAuthenticationFilter() throws Exception{
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return authenticationFilter;
    }

}
