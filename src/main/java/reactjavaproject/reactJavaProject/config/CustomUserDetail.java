package reactjavaproject.reactJavaProject.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import reactjavaproject.reactJavaProject.entity.Users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetail implements UserDetails {
    private final Users users;

    public CustomUserDetail(Users users) {
        this.users = users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(users.getRole());
        return List.of(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Users getUser() {
        return users;
    }
}
