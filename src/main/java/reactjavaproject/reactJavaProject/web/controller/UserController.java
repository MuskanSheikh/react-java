package reactjavaproject.reactJavaProject.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactjavaproject.reactJavaProject.services.UsersService;
import reactjavaproject.reactJavaProject.web.dto.UserDTO;

@RestController
@RequestMapping("user-api/")
public class UserController {
    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }
    @PostMapping("create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN_USER')")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        UserDTO userResponse = usersService.createUser(userDTO);
        if(userResponse != null){
            return ResponseEntity.ok("user created");
        }
        return ResponseEntity.badRequest().body("fail to create user");
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        UserDTO response = usersService.getById(id);
        if(response != null){
            response.setPassword("");
        return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(" user not found");
    }
}
