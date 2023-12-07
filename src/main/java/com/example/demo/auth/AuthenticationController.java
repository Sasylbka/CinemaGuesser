package com.example.demo.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidParameterException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws AuthenticationException {

        return ResponseEntity.ok(service.authenticate(request));
    }


    @ExceptionHandler(InvalidParameterException.class)
    public String handleException(InvalidParameterException e) {
        return e.getMessage();
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendResetPasswordRequest(
            @RequestBody EmailRequest request
    ) {
        try {
            service.sendResetPasswordRequestToUser(request.getEmail());
            return ResponseEntity.ok("Код восстановления пароля отправлен на " + request.getEmail());
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Нет пользователя с таким email");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при попытке отправки кода восстановления пароля");
        }
    }

    @PostMapping("/check-pass-reset-code")
    public ResponseEntity<String> checkPasswordResetCode(
            @RequestBody PasswordResetCodeRequest request
    ) {
        try {
            service.checkPasswordResetCode(request.getEmail(), request.getResetCode());
            return ResponseEntity.ok("Код восстановления пароля принят");
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Срок действия кода восстановления пароля истёк");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Неверный код восстановления пароля");
        }
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<String> setNewPassword(
            @RequestBody SetNewPasswordRequest request
    ) {
        try {
            service.setNewPassword(request.getEmail(), request.getResetCode(), request.getNewPassword());
            return ResponseEntity.ok("Пароль успешно изменён");
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Срок действия кода восстановления пароля истёк");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Неверный код восстановления пароля");
        }
    }
}

