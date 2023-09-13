package com.neshan.trafficreporter.controller;

import com.neshan.trafficreporter.dto.AuthenticationDto;
import com.neshan.trafficreporter.dto.UserDto;
import com.neshan.trafficreporter.request.AuthenticationRequest;
import com.neshan.trafficreporter.request.RegisterRequest;
import com.neshan.trafficreporter.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/v1/auth/register")
    public ResponseEntity<AuthenticationDto> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthenticationDto> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/v1/auth")
    public ResponseEntity<UserDto> get() {
        return ResponseEntity.ok(authenticationService.get());
    }
}
