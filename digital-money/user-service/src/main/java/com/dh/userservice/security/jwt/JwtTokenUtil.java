package com.dh.userservice.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {

    private static final Key ACCESS_TOKEN_SECRET= Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final static  long ACCESS_TOKEN_VALIDITY_SECONDS = 2_592_000L;

    public static String createToken(String nombre, String email){
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", nombre);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getEncoded()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(String token){
       try {
           Claims claims = Jwts.parserBuilder()
                   .setSigningKey(ACCESS_TOKEN_SECRET)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();

           String email = claims.getSubject();

           return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
       } catch (JwtException e){
           return null;
       }
    }
}
