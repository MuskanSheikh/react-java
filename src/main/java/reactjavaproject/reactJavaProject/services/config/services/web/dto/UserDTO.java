package reactjavaproject.reactJavaProject.services.config.services.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactjavaproject.reactJavaProject.services.config.services.entity.Users;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;

    public static UserDTO getEntity(Users users) {
        UserDTO userDTO= new UserDTO();
        userDTO.setFirstName(users.getFirstName());
        userDTO.setLastName(users.getLastName());
        userDTO.setEmail(users.getEmail());
        userDTO.setPhone(userDTO.getPhone());
        return userDTO;
    }
}
