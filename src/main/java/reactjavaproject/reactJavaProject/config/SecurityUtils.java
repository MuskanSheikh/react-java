package reactjavaproject.reactJavaProject.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityUtils {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final PasswordEncoder passwordEncoder;

    public SecurityUtils(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }


    public static Long getCurrentUserId() {
        CustomUserDetail user = getCurrentUser();
        if(user != null && user.getUser() != null){
            return user.getUser().getId();
        }
        return null;
    }

    public static CustomUserDetail getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof CustomUserDetail) {
            return (CustomUserDetail) authentication.getPrincipal();
//            }
        }
        return null;
    }

//    public Authentication authenticate(Authentication auth) throws AuthenticationException {
//        String username = auth.getName();
//        String password = auth.getCredentials().toString();
//
//        CustomUserDetail user = (CustomUserDetail) userDetailsServiceImpl.loadUserByUsername(username.toLowerCase());
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
//        }
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Unauthorized");
//    }
}
