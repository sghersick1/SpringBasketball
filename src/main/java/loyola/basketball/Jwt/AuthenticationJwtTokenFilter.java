package loyola.basketball.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AuthenticationJwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public AuthenticationJwtTokenFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    /**
     * Filter a authentication for JWT token
     * Spring's DelegatingFilterProxy automatically passes in filterChain
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        // Validate JWT token from Authorization header
        String bearer = request.getHeader("Authorization");

        if(bearer == null || !bearer.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token from String
        String token = bearer.substring(7); // Remove "Bearer"
        System.out.println(token);
        System.out.println(jwtUtils.isValid(token));

        if(jwtUtils.isValid(token)) {
            String username = jwtUtils.extractUsername(token);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    jwtUtils.extractUsername(token),
                    "password",
                    jwtUtils.extractAuthorities(token));

            // Set Security Context using Holder
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request, response);
    }
}
