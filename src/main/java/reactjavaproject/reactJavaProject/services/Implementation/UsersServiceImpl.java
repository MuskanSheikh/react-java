package reactjavaproject.reactJavaProject.services.Implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
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
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {


    private final BCryptPasswordEncoder encoder;

    private final UserRepository userRepository;


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        Optional<Users> userEntity = userRepository.findByEmail(userDTO.getEmail());
        if (!userEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Already exist");
        Users users = Users.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .password(encoder.encode(userDTO.getPassword()))
                .role(String.valueOf(EnumUtils.USER)).build();
        userRepository.save(users);
        return getUserModel(users);
    }

    @Override
    public UserDTO getById(Long id) {
        Optional<Users> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return getUserModel(userOptional.get());
        }
        return null;
    }

    private UserDTO getUserModel(Users entity) {
        if (entity == null) throw new UsernameNotFoundException("User does not exist");
        return new UserDTO(entity);
    }
}
