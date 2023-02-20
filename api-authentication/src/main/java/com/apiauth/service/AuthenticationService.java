package com.apiauth.service;

import com.apiauth.dto.AuthenticationRequest;
import com.apiauth.dto.AuthenticationResponse;
import com.apiauth.dto.RegisterRequest;
import com.apiauth.model.Role;
import com.apiauth.model.User;
import com.apiauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(registerRequest.getPassword()))
                .role(Role.User)
                .build();
        userRepository.save(user);

       return authenticationResponseWithJwt(user);
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        Authentication authenticateUser = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        if (authenticateUser.isAuthenticated()) {
            User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
            return authenticationResponseWithJwt(user);
        } else {
            throw new IllegalArgumentException("Invalid user request");
        }
    }

    private AuthenticationResponse authenticationResponseWithJwt(UserDetails userDetails){
        String jwtToken = jwtService.generateJwtToken(userDetails);
        return AuthenticationResponse.builder().jwtToken(jwtToken).build();
    }

}
