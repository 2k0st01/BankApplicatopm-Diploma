package com.example.bankapplicatopm.restController;

import com.example.bankapplicatopm.componet.AuthenticationResponse;
import com.example.bankapplicatopm.dto.account.AuthRequest;
import com.example.bankapplicatopm.dto.account.RegistrationRequestUserApplicationDTO;
import com.example.bankapplicatopm.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final RegistrationService registrationService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequestUserApplicationDTO request
    ) {
        return ResponseEntity.ok(registrationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> aunt(
            @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(registrationService.auth(request));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> aunt(){
        return ResponseEntity.ok("hello");
    }

}
