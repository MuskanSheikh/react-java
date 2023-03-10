package reactjavaproject.reactJavaProject.services.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactjavaproject.reactJavaProject.common.EnumUtils;
import reactjavaproject.reactJavaProject.entity.Users;
import reactjavaproject.reactJavaProject.repository.UserRepository;
import reactjavaproject.reactJavaProject.services.UsersService;
import reactjavaproject.reactJavaProject.web.dto.UserDTO;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {


    private final BCryptPasswordEncoder encoder;

    private final UserRepository userRepository;



    public UsersServiceImpl(BCryptPasswordEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        Optional<Users> userEntity = userRepository.findByEmail(userDTO.getEmail());
        if (!userEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Already exist");
        Users users = new Users();
        users.setFirstName(userDTO.getFirstName());
        users.setLastName(userDTO.getLastName());
        users.setEmail(userDTO.getEmail());
        users.setPhone(userDTO.getPhone());
        users.setPassword(encoder.encode(userDTO.getPassword()));
        users.setRole(String.valueOf(EnumUtils.USER));
        userRepository.save(users);
        return getUserModel(users);
    }

    @Override
    public UserDTO getById(Long id) {
        Optional<Users> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
//            UserDetails userDetails =  userDetailsService.loadUserByUsername(userOptional.get().getEmail());
//            return getUserModel(userObj);
        }
        return null;
    }

    private UserDTO getUserModel(Users entity) {
        if (entity == null) throw new UsernameNotFoundException("User does not exist");
        return new UserDTO(entity);
    }
}
