package reactjavaproject.reactJavaProject.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactjavaproject.reactJavaProject.config.JwtTokenUtil;
import reactjavaproject.reactJavaProject.entity.Users;
import reactjavaproject.reactJavaProject.repository.UserRepository;
import reactjavaproject.reactJavaProject.web.dto.AuthenticationRequest;
import reactjavaproject.reactJavaProject.web.dto.LoginResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;


    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws NoSuchAlgorithmException, InvalidKeyException {
         Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails users = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(users);

        LoginResponse loginResponse = new LoginResponse();
        Optional<Users> optionalEntity = userRepository.findByEmail(authenticationRequest.getUserName());
        loginResponse.setToken(token);
        loginResponse.setType("Bearer");
        loginResponse.setRole(optionalEntity.get().getRole());
        return ResponseEntity.ok(loginResponse);
    }
}
