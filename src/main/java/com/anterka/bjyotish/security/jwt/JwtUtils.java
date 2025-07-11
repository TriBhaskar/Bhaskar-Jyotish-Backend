package com.anterka.bjyotish.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Getter
    private long jwtExpirationTimeInMillis = 1000 * 60 * 60 * 2; // 2 hours
    private final Environment env;

    private final Key signingKey;

    public JwtUtils(Environment env) {
        this.env = env;
        this.signingKey = initializeSigningKey();
    }

    /**
     * Extracts the user email from the JWT token in the request
     */
    public String extractUserName(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    /**
     * Validates the token w.r.t the [{@link UserDetails}] along with the expiration date
     */
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * generates the token just with the userDetails if there are no Claims
     */
    public String generateJwtToken(String userPrincipal) {
        return generateJwtToken(new HashMap<>(), userPrincipal);
    }

    /**
     * generates the token including the claims [RegisterClaims, PublicClaims, PrivateClaims]
     * Fetches the expiration time in hours from the properties
     * Default expiration time is [2] hours
     */
    public String generateJwtToken(Map<String, Object> claims, @NonNull String userPrincipal) {
        int expirationTimeInHours = Integer.parseInt(env.getProperty("jwt.security.secret-key.expiration.time.in-hours", "2"));
        jwtExpirationTimeInMillis = 1000L * 60 * 60 * expirationTimeInHours;
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTimeInMillis))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * @return true if the token is not expired by reading the expiration from the claims
     * */
    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    /**
     * extracts the expiration from the claims with the token
     */
    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * extract single claim passed with the token
     */
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsForToken(jwtToken);
        return claimsResolver.apply(claims);
    }

    /**
     * extract all claims associated with the token
     */
    private Claims extractAllClaimsForToken(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwtToken).getBody();
    }

    /**
     * reads the secret_key from the properties
     * @return Key using [SH256 signature Algorithm]
     * */
    private Key getSignInKey() {
        return signingKey;
    }

    private Key initializeSigningKey() {
        final String secretKey = env.getProperty("jwt.security.secret-key");
        byte[] signInKeyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(signInKeyBytes);
    }
}
