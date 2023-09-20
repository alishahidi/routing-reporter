package com.neshan.routingreporter.service;

import com.neshan.routingreporter.component.ReportFactory;
import com.neshan.routingreporter.dto.AuthenticationDto;
import com.neshan.routingreporter.dto.UserDto;
import com.neshan.routingreporter.enums.Role;
import com.neshan.routingreporter.mapper.AuthenticationMapper;
import com.neshan.routingreporter.mapper.RouteMapper;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.repository.UserRepository;
import com.neshan.routingreporter.request.AuthenticationRequest;
import com.neshan.routingreporter.request.RegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

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

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole())
                .reports(user.getReports()
                        .stream()
                        .map(ReportFactory::mapToMapper)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
                )
                .routes(user.getRoutes().stream().map(RouteMapper.INSTANCE::routeToRouteDto).toList())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
