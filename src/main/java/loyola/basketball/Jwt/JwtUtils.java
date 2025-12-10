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

    /**
     * Generate Access Token (expires after ~25 minutes)
     * @param authentication Users Authentication Details
     * @return Signed JWT Access Token
     */
    public String generateAccessToken(Authentication authentication){
        Date expiry = new Date(new Date().getTime() + accessExp.toMillis());
        return generateToken(authentication, expiry);
    }

    /**
     * Generate Refresh Token (expires after ~30 days)
     * @param authentication Users Authentication Details
     * @return Signed JWT Refresh Token
     */
    public String generateRefreshToken(Authentication authentication){
        Date expiry = new Date(new Date().getTime() + refreshExp.toMillis());
        return generateToken(authentication, expiry);
    }

    /**
     * Generate a JWT Token
     * @param authentication Users Authentication Details
     * @param expiry Duration of Time Until Token Expires
     * @return Signed JWT Token
     */
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

    /**
     * Extract Username from JWT Token
     * @param token JWT Token
     * @return User's Username
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Extract User Authorities From JWT Token
     * @param token JWT Token
     * @return Collection of User's Roles (ex: USER, ADMIN)
     */
    public Collection<? extends GrantedAuthority> extractAuthorities(String token){
         Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                 .getPayload();

         List<String> roles = claims.get("roles", List.class);
         roles.forEach(System.out::println);
         return roles.stream()
                 .map(SimpleGrantedAuthority::new)
                 .toList();
    }

    /**
     * Verify a JWT Token:
     * 1) Signed With Secret Key
     * 2) Not Expired
     * @param token JWT Token
     * @return True When Passes Both Conditions, False Otherwise
     */
    public boolean isValid(String token){
        try{
            return !extractExpiration(token).before(new Date());
        }catch (Exception e) { // Token doesn't have expiration
            System.out.println("INVALID TOKEN - unable to extract expiration");
            return false;
        }
    }

    /**
     * Extract Expiration Date From JWT Token
     * @param token JWT Token
     * @return Expiration Date
     */
    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getExpiration();
    }

    /**
     * Generate Signing Key from jwtSecret String
     * @return Signing Key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
