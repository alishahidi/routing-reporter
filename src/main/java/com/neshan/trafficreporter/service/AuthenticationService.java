package com.neshan.trafficreporter.service;

import com.neshan.trafficreporter.dto.AuthenticationDto;
import com.neshan.trafficreporter.dto.UserDto;
import com.neshan.trafficreporter.enums.Role;
import com.neshan.trafficreporter.mapper.AuthenticationMapper;
import com.neshan.trafficreporter.mapper.UserMapper;
import com.neshan.trafficreporter.model.User;
import com.neshan.trafficreporter.repository.UserRepository;
import com.neshan.trafficreporter.request.AuthenticationRequest;
import com.neshan.trafficreporter.request.RegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    AuthenticationManager authenticationManager;
    UserRepository userRepository;

    public AuthenticationDto register(RegisterRequest request) {
        User user = User
                .builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User createdUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationMapper.INSTANCE.authenticationToAuthenticationDto(createdUser, jwtToken);
    }

    public AuthenticationDto authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationMapper.INSTANCE.authenticationToAuthenticationDto(user, jwtToken);
    }

    public UserDto get() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return UserMapper.INSTANCE.userToUserDTO(user);
    }
}
