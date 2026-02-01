package com.github.pmbdev.global_finance_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component // Calling Spring
public class JwtUtils {

    // Read properties of application.yaml
    @Value("${jwt.secret}") // Take the secret "password"
    private String secretKey;

    @Value("${jwt.expiration}") // Take the time of expiration
    private long jwtExpiration;

    // Generate Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // The token's owner
                .setIssuedAt(new Date(System.currentTimeMillis())) // Creation time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Expiration time
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Digital signature,
                // if someone tries to modify the expiration time or the username without the key, the signature breaks
                .compact();
    }

    // Validate Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, String username){
        final String usernameInToken = extractUsername(token);
        return (usernameInToken.equals(username) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    // --- Other methods ---

    // Transforms the text from the YAML file into a cryptographic key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extracts generic info from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extracts all the info from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}