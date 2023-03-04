package reactjavaproject.reactJavaProject.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import reactjavaproject.reactJavaProject.config.JWTAuthHelper;
import reactjavaproject.reactJavaProject.entity.Users;
import reactjavaproject.reactJavaProject.services.UsersService;
import reactjavaproject.reactJavaProject.web.dto.AuthenticationRequest;
import reactjavaproject.reactJavaProject.web.dto.LoginResponse;
import reactjavaproject.reactJavaProject.web.dto.UserDTO;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    private final UsersService usersService;

    private final AuthenticationManager authenticationManager;

    private final JWTAuthHelper jwtAuthHelper;

    public UserController(UsersService usersService, AuthenticationManager authenticationManager, JWTAuthHelper jwtAuthHelper) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtAuthHelper = jwtAuthHelper;

    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO){
        UserDTO userResponse = usersService.createUser(userDTO);
        if(userResponse != null){
            return ResponseEntity.ok("user created");
        }
        return ResponseEntity.badRequest().body("fail to create user");
    }
    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws NoSuchAlgorithmException, InvalidKeyException {
         Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails users = (UserDetails) authentication.getPrincipal();
        String token = jwtAuthHelper.generateToken(users.getUsername());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return ResponseEntity.ok(loginResponse);
    }
}
