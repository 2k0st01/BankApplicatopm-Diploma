package com.example.bankapplicatopm.restController;

import com.example.bankapplicatopm.dto.account.EmailDTO;
import com.example.bankapplicatopm.dto.account.PasswordChangeDto;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.service.BankAccountService;
import com.example.bankapplicatopm.util.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@AllArgsConstructor
public class ForgotPassword {
    private final BankAccountService bankAccountService;

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody EmailDTO emailDTO) {
        String email = emailDTO.getEmail();
        try {
            BankAccount user = bankAccountService.forgotPassword(email);
            return ResponseEntity.ok(new ApiResponse(true, "Massages send on your Email", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false,
                    "We don't found users with this email: " + e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDto passwordChangeDto,
                                                 @RequestParam("token") String token) {
        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid token.");
        }

        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }

        bankAccountService.changeForgetPasswordByToken(token, passwordChangeDto.getNewPassword());

        return ResponseEntity.ok("Password successfully changed.");
    }
}
