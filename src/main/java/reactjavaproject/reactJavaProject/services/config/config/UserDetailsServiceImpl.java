package reactjavaproject.reactJavaProject.services.config.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactjavaproject.reactJavaProject.services.config.entity.Users;
import reactjavaproject.reactJavaProject.services.config.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmailIgnoreCase(email);
        if(users == null)
        {
            throw new UsernameNotFoundException("Could not found user");
        }
//        CustomUserDetail customUserDetail=new CustomUserDetail(users);
        return new CustomUserDetail(users);
    }
}
