package reactjavaproject.reactJavaProject.services.services.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactjavaproject.reactJavaProject.services.entity.Users;
import reactjavaproject.reactJavaProject.services.repository.UserRepository;
import reactjavaproject.reactJavaProject.services.services.UsersService;
import reactjavaproject.reactJavaProject.services.web.dto.UserDTO;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {


    private  final BCryptPasswordEncoder encoder;

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
        userRepository.save(users);
        return UserDTO.getEntity(users);
    }
}
