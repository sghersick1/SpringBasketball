package loyola.basketball.Controller;

import loyola.basketball.Jwt.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JdbcUserDetailsManager userDetailsService;

    public SecurityController(PasswordEncoder passwordEncoder, JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = (JdbcUserDetailsManager) userDetailsService;
    }

    /**
     * Register a new user and return them a JWT token
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password){
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
        return ResponseEntity.created(URI.create("/auth/register")).body(jwtToken);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(){return null;}
}
