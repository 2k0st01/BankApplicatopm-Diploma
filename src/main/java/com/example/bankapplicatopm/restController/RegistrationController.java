package com.example.bankapplicatopm.restController;

import com.example.bankapplicatopm.dto.account.RegistrationRequestUserApplicationDTO;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.service.RegistrationService;
import com.example.bankapplicatopm.util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequestUserApplicationDTO request){
        try {
            BankAccount user = registrationService.register(request);
            return ResponseEntity.ok(new ApiResponse(true, "Registration successful", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Registration failed: " + e.getMessage()));
        }
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token){
        return  registrationService.confirmToken(token);
    }
}
