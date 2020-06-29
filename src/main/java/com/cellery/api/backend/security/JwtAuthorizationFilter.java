package com.cellery.api.backend.security;

import com.cellery.api.backend.shared.Util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private Environment environment;
    private JwtUtil jwtUtil;

    public JwtAuthorizationFilter(Environment environment, AuthenticationManager authManager, JwtUtil jwtUtil) {
        super(authManager);
        this.environment = environment;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers," +
                " X-Requested-With, Origin, X-Auth-Token, Tenant, request_date, token");
        String authorizationHeader = request.getHeader(environment.getProperty("authentication.authorization"));
        if (authorizationHeader == null || !authorizationHeader.startsWith(environment.getProperty("authentication.bearer"))) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String authorizationHeader = req.getHeader(environment.getProperty("authentication.authorization"));
        if (authorizationHeader == null) {
            return null;
        }
        String token = authorizationHeader.replace(environment.getProperty("authentication.bearer"), "");

        try {
            String email = jwtUtil.getEmailFromToken(token);

            if (email == null) {
                return null;
            }
            return new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

        } catch (JwtException e) {
            return null;
        }
    }
}
