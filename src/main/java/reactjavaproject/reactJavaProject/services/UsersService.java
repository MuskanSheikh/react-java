package reactjavaproject.reactJavaProject.services;

import reactjavaproject.reactJavaProject.web.dto.UserDTO;


public interface UsersService {
    public UserDTO createUser(UserDTO userDTO);

    UserDTO getById(Long id);
}
