package loyola.basketball.JwtTests;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import loyola.basketball.Jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    private String testJwtSecret;

    @BeforeEach
    public void setUp() throws Exception {
        // Generate a test secret - 256 bit String
        byte[] keyBytes = new byte[32];
        new java.security.SecureRandom().nextBytes(keyBytes);
        testJwtSecret = java.util.Base64.getEncoder().encodeToString(keyBytes);
        
        // Set the jwtSecret field directly (use Field because it's private)
        Field jwtSecretField = JwtUtils.class.getDeclaredField("jwtSecret");
        jwtSecretField.setAccessible(true);
        jwtSecretField.set(jwtUtils, testJwtSecret);
    }

    /* Is Valid Tests */
    @Test
    public void isValid_ValidTokenNotExpired_ReturnsTrue(){
        // Arrange
        String username = "testuser";
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getName()).thenReturn(username);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        
        // Generate a valid token that hasn't expired
        String validToken = jwtUtils.generateAccessToken(authentication);
        
        // Act
        boolean result = jwtUtils.isValid(validToken);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void isValid_ExpiredToken_ReturnsFalse(){
        // Arrange
        String username = "testuser";
        
        // Create a token with an expiry date in the past
        Date pastExpiry = new Date(System.currentTimeMillis() - 10000);
        SecretKey signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(testJwtSecret));
        String expiredToken = Jwts.builder()
                .subject(username)
                .claim("roles", List.of("ROLE_USER"))
                .issuedAt(new Date(System.currentTimeMillis() - 20000))
                .expiration(pastExpiry)
                .signWith(signingKey)
                .compact();
        
        // Act
        boolean result = jwtUtils.isValid(expiredToken);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void isValid_InvalidToken_ReturnsFalse(){
        // Arrange
        String invalidToken = "invalid.token.string";
        
        // Act
        boolean result = jwtUtils.isValid(invalidToken);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void isValid_TokenWithoutExpiration_ReturnsFalse(){
        // Arrange
        String username = "testuser";
        SecretKey signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(testJwtSecret));
        
        // Create a token without expiration claim
        String tokenWithoutExpiration = Jwts.builder()
                .subject(username)
                .claim("roles", List.of("ROLE_USER"))
                .issuedAt(new Date())
                // No expiration set
                .signWith(signingKey)
                .compact();
        
        // Act
        boolean result = jwtUtils.isValid(tokenWithoutExpiration);
        
        // Assert
        assertFalse(result);
    }
}
