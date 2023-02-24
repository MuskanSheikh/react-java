package reactjavaproject.reactJavaProject.services.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactjavaproject.reactJavaProject.entity.Users;
import reactjavaproject.reactJavaProject.repository.UserRepository;
import reactjavaproject.reactJavaProject.services.UsersService;
import reactjavaproject.reactJavaProject.web.dto.UserDTO;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    public UsersServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
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
        users.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(users);
        return UserDTO.getEntity(users);
    }
}
