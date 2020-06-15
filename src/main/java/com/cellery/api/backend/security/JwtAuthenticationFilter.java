package com.cellery.api.backend.security;

import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.ui.model.request.LoginRequestModel;
import com.cellery.api.backend.ui.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UsersService usersService;
    private Environment environment;

    public JwtAuthenticationFilter(Environment environment, UsersService usersService, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((User) authResult.getPrincipal()).getUsername();

        UserDto userDetails = usersService.getUserDetailsByEmail(email);

        System.out.println(environment.getProperty("authentication.jwt.secret"));
        //Generate JWT tokens
        String token = Jwts.builder()
                        .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong("12312414124")))
                .signWith(SignatureAlgorithm.HS512,environment.getProperty("authentication.jwt.secret"))
                .compact();
        response.addHeader("token",token);
        response.addHeader("userId",userDetails.getUserId());
    }
}
