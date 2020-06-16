package com.cellery.api.backend.shared.Util;

import com.cellery.api.backend.shared.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil implements Serializable {

    private static final long serialVersionUID = 286506925331286664L;

    @Autowired
    private Environment env;

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(env.getProperty("authentication.jwt.secret")).parseClaimsJws(token).getBody();
    }

    private Boolean isExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong("12312414124")))
                .signWith(SignatureAlgorithm.HS512,env.getProperty("authentication.jwt.secret"))
                .compact();
    }

    public Boolean validateToken(String token, UserDto userDetails) {
        final String userEmail = getEmailFromToken(token);
        return userEmail.equals(userDetails.getEmail()) && !isExpired(token);
    }
}
