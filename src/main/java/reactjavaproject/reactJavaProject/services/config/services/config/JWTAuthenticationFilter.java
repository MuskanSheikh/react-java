package reactjavaproject.reactJavaProject.services.config.services.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
        String username = null;
        String jwtToken = null;
        final String authToken = jwtAuthHelper.getAuthHeaderFromHeader(request);
        if (authToken != null) {
            jwtToken = jwtAuthHelper.getToken(request);
            try {
                username = jwtAuthHelper.getUserFromTokenString(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }
        if (username != null) {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtAuthHelper.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

//            if(userName != null){
//                final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//                if(jwtAuthHelper.validateToken(authToken,userDetails)){
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authentication.setDetails(new WebAuthenticationDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//        }
//        filterChain.doFilter(request,response);

//        final String requestTokenHeader = request.getHeader("Authorization");

//        String jwtToken = null;
//        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
//        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//            jwtToken = requestTokenHeader.substring(7);
//            try {
//                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//            } catch (IllegalArgumentException e) {
//                System.out.println("Unable to get JWT Token");
//            } catch (ExpiredJwtException e) {
//                System.out.println("JWT Token has expired");
//            }
//        } else {
//            logger.warn("JWT Token does not begin with Bearer String");
//        }

//Once we get the token validate it.
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // if token is valid configure Spring Security to manually set authentication
//            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken
//                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                // After setting the Authentication in the context, we specify
//                // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
}
