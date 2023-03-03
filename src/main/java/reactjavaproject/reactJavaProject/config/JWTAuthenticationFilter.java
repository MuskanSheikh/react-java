package reactjavaproject.reactJavaProject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JWTAuthHelper jwtAuthHelper;

    public JWTAuthenticationFilter(UserDetailsService userDetailsService, JWTAuthHelper jwtAuthHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthHelper = jwtAuthHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = jwtAuthHelper.getAuthHeaderFromHeader(request);
        if(authToken != null){
            String userName = jwtAuthHelper.getUserFromTokenString(authToken);
            if(userName != null){
                final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if(jwtAuthHelper.validateToken(authToken,userDetails)){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
