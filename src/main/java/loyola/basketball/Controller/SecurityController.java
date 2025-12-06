package loyola.basketball.Controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password){
        // Ensure username isn't taken
        if(userDetailsService.userExists(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 is standard for conflicts like username taken
                    .body(Map.of(
                            "error", "USERNAME_TAKEN",
                            "message", "The username '" + username + "' is already in use.",
                            "username", username
                    ));
        }

        // Create user in database
        UserDetails user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();

        userDetailsService.createUser(user);

        // Create JWT token for user
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                user.getAuthorities()
        );
        String jwtToken = jwtUtils.generateToken(authentication);

        // Return JWT token
        return ResponseEntity.created(URI.create("/auth/register")).body(Map.of(
                "accessToken", jwtToken,
                "tokenType", "Bearer"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password){
        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String jwtToken = jwtUtils.generateToken(auth);
            return ResponseEntity.ok(Map.of(
                    "accessToken", jwtToken,
                    "tokenType", "Bearer"
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
}
