package reactjavaproject.reactJavaProject.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactjavaproject.reactJavaProject.entity.Users;

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
    private String role;

    public UserDTO(Users entity) {
        this.id = entity.getId();
        this.firstName = entity.getFirstName();
        this.lastName = entity.getLastName();
        this.email = entity.getEmail();
        this.phone= entity.getPhone();
        this.password = entity.getPassword();
    }

    public static UserDTO getEntity(Users users) {
        UserDTO userDTO= new UserDTO();
        userDTO.setFirstName(users.getFirstName());
        userDTO.setLastName(users.getLastName());
        userDTO.setEmail(users.getEmail());
        userDTO.setPhone(userDTO.getPhone());
        return userDTO;
    }
}
