package reactjavaproject.reactJavaProject.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactjavaproject.reactJavaProject.services.UsersService;
import reactjavaproject.reactJavaProject.web.dto.UserDTO;

@RestController
@RequestMapping("api/")
public class UserController {
    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }
    @PostMapping("create")
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
        return ResponseEntity.notFound().build();
    }
}
