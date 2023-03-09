package reactjavaproject.reactJavaProject.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactjavaproject.reactJavaProject.config.JwtTokenUtil;
import reactjavaproject.reactJavaProject.web.dto.AuthenticationRequest;
import reactjavaproject.reactJavaProject.web.dto.LoginResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/v1/")
public class AuthorityController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    public AuthorityController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws NoSuchAlgorithmException, InvalidKeyException {
         Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails users = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(users);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return ResponseEntity.ok(loginResponse);
    }
}
