package nvt.kts.project.security;

import nvt.kts.project.util.TokenUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.jsonwebtoken.ExpiredJwtException;


public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;

    private final UserDetailsService userDetailsService;

    protected final Log LOGGER = LogFactory.getLog(getClass());

    public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService) {
        this.tokenUtils = tokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username;
        String authToken = tokenUtils.getToken(request);
        System.out.print("pozvalo inteeeeeeeeeeeeeeeeeeeeeeernalll");
        System.out.print("pozvalo tokeeeeen "+ authToken);

        try {
            if (authToken != null) {
                username = tokenUtils.getUsernameFromToken(authToken);
                System.out.print(username);
                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    System.out.print(userDetails);
                    if (userDetails == null)
                        throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));

                    if (tokenUtils.validateToken(authToken, userDetails)) {
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

        } catch (ExpiredJwtException ex) {
            LOGGER.debug("Token expired!");
        }

        filterChain.doFilter(request, response);
    }
}
