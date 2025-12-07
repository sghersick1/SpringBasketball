package loyola.basketball.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.Principal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    @Value("${JWT_SECRET_KEY}")
    private String jwtSecret;
    private final Duration accessExp = Duration.ofMinutes(25);
    private final Duration refreshExp = Duration.ofDays(30);

    // Generate user token with
    public String generateAccessToken(Authentication authentication){
        Date expiry = new Date(new Date().getTime() + accessExp.toMillis());
        return generateToken(authentication, expiry);
    }

    public String generateRefreshToken(Authentication authentication){
        Date expiry = new Date(new Date().getTime() + refreshExp.toMillis());
        return generateToken(authentication, expiry);
    }

    private String generateToken(Authentication authentication, Date expiry){
        // Need to save roles as strings (not GrantedAuthority Objects)
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getSubject();
    }

    // Extract authorities
    public Collection<? extends GrantedAuthority> extractAuthorities(String token){
         Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                 .getPayload();

         List<String> roles = claims.get("roles", List.class);
         return roles.stream()
                 .map(SimpleGrantedAuthority::new)
                 .toList();
    }

    // Verify token has expiration time
    public boolean isValid(String token){
        try{
            return !extractExpiration(token).before(new Date());
        }catch (Exception e) { // Token doesn't have expiration
            System.out.println("INVALID TOKEN - unable to extract expiration");
            return false;
        }
    }

    // Extract token expiration
    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getExpiration();
    }

    SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
