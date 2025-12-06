package loyola.basketball.Config;

import loyola.basketball.Jwt.AuthenticationJwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    // Configure data source from application.properties
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationJwtTokenFilter jwtTokenFilter;

    /**
     * Custom filter chain
     * @param http requests
     * @return Security Filter Chain
     * @throws Exception with Auth
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((req) -> req
                .requestMatchers("/team/**", "/game/**").authenticated()
                .anyRequest().permitAll()
        );
        http.sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)));
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.cors(cors -> cors.disable());
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configure user details from MySQL db
     * @return user details
     */
    @Bean
    public UserDetailsService userDetails(){
        return new JdbcUserDetailsManager(dataSource);
    }

    /**
     * Add password storage Encryption
     * @return password encoder object
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Expose Authentication Manager bean from configuration (UserDetailsService bean, PasswordEncoder bean, ...)
     * @return Configured Authentication Manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
