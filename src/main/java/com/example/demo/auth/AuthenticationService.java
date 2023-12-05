package com.example.demo.auth;

import com.example.demo.config.JwtService;
import com.example.demo.config.EmailService;
import com.example.demo.token.Token;
import com.example.demo.Repositories.TokenRepository;
import com.example.demo.token.TokenType;
import com.example.demo.user.User;
import com.example.demo.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.naming.directory.InvalidAttributesException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    @Value("${application.security.reset-password-expiration}")
    private long resetPasswordExpiration;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var temp = repository.findByUsername(request.getUsername());
            if(temp.isPresent()){
                throw new InvalidParameterException("Пользователь с таким логином уже есть.");
            }
            else {
                temp=repository.findByEmail(request.getEmail());
                if(temp.isPresent()){
                    throw new InvalidParameterException("Пользователь с такой почтой уже есть.");
                }
            }
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        }
        catch (AuthenticationException e){
            throw new InvalidParameterException("Ошибка авторизации. Проверьте правильонсть ввода данных.");
        }

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();



        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void sendResetPasswordRequestToUser(String email) throws Exception {
        User user = repository
                .findByEmail(email)
                .orElseThrow(() -> new InvalidParameterException("Нет пользователя с таким email."));
        
        // создать четырёхзначный код для восстановления пароля
        user.setPasswordResetCode((int) ((Math.random() * (10000 - 1000)) + 1000));
        try {
            emailService.sendResetPasswordRequestToUser(email, user.getUsername(), user.getPasswordResetCode());
        } catch (Exception e) {
            throw new Exception("Ошибка отправки кода восстановления пароля пользователю с email " + email);
        }
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.repository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
