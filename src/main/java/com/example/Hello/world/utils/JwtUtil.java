package com.example.Hello.world.utils;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Component
//this class is to generate and validate JWT token
public class JwtUtil {
    private final String SECRET="Hardwork pays off, consistency keeps you in loop. Keep crushing the lazy days!"; // like app pswrd, tokens r created using this secret
    private final long EXPIRATION =1000*60; // Created token will be valid for 1000*30 seconds,after this time, token expires. On re-login -> new token will be created.
    private final Key secretKey= Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));//converts each char to byte
    //Sha - encryption algorithm.

    //To generate token
    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) //adds the current time with expiration time we set, so that untl that time the token will be valid.
                .signWith(secretKey, SignatureAlgorithm.HS256) //algorithm - digital signature ->like sealing token
                .compact();
    }
    public String extractEmail(String token){
        return //Like opening the seal and validating
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token) // it is said as valid token, you once check whether its truely valid
                        .getBody()
                        .getSubject();
    }
    //to validate the token
    public boolean validateJwtToken(String token){
        try{
            extractEmail(token);
            return true;
        }catch(JwtException exception){
            return false;
        }
    }

}
