package com.neshan.routingreporter.controller;

import com.neshan.routingreporter.dto.AuthenticationDto;
import com.neshan.routingreporter.dto.UserDto;
import com.neshan.routingreporter.request.AuthenticationRequest;
import com.neshan.routingreporter.request.RegisterRequest;
import com.neshan.routingreporter.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationV1Controller {

    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDto> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping
    public ResponseEntity<UserDto> get() {
        return ResponseEntity.ok(authenticationService.get());
    }
}