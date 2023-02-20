package com.apiauth.controller;

import com.apiauth.dto.AuthenticationRequest;
import com.apiauth.dto.AuthenticationResponse;
import com.apiauth.dto.RegisterRequest;
import com.apiauth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;
    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.registerUser(registerRequest));
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticateUser(authenticationRequest));
    }
}
