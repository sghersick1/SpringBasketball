package loyola.basketball.Controller;

import loyola.basketball.Entity.RefreshTokenRequest;
import loyola.basketball.Entity.UserRequest;
import loyola.basketball.Jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JdbcUserDetailsManager userDetailsService;
    private final AuthenticationManager authenticationManager;

    public SecurityController(PasswordEncoder passwordEncoder, JwtUtils jwtUtils, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = (JdbcUserDetailsManager) userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Register a new user and return them a JWT token
     * @param user user details
     * @return access + refresh token
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest user){
        // Ensure username isn't taken
        if(userDetailsService.userExists(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 is standard for conflicts like username taken
                    .body(Map.of(
                            "error", "USERNAME_TAKEN",
                            "message", "The username '" + user.getUsername() + "' is already in use.",
                            "username", user.getUsername()
                    ));
        }

        // Create user in database
        UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles("USER")
                .build();

        userDetailsService.createUser(userDetails);

        // Create JWT token for user
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);

        // Return JWT token
        return ResponseEntity.created(URI.create("/auth/register")).body(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken));
    }

    /**
     * Login a user
     * @param user user details
     * @return access + refresh token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest user){
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            String accessToken = jwtUtils.generateAccessToken(auth);
            String refreshToken = jwtUtils.generateRefreshToken(auth);
            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        }catch (AuthenticationException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "INVALID_CREDENTIALS",
                            "message", "Invalid username or password."
                    ));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();
        if (jwtUtils.isValid(refreshToken)) {
            Authentication auth = new UsernamePasswordAuthenticationToken(jwtUtils.extractUsername(refreshToken), null);
            String newAccessToken = jwtUtils.generateAccessToken(auth);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        }
        return ResponseEntity.status(403).body("Invalid refresh token");
    }
}
