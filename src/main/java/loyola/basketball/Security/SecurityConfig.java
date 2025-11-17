package loyola.basketball.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    // Configure data source from application.properties
    @Autowired
    DataSource dataSource;

    /**
     * Custom filter chain
     * @param http requests
     * @return Security Filter Chain
     * @throws Exception with Auth
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((req) -> req
                .requestMatchers("/team/**").authenticated()
                .requestMatchers("/unauthorized/**").permitAll()
        );
        http.sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)));
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
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
}
