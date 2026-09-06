package org.example.backend_vet_in_house.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.users.dto.req.*;
import org.example.backend_vet_in_house.users.dto.res.LoginResDTO;
import org.example.backend_vet_in_house.users.service.AuthService;
import org.example.backend_vet_in_house.users.service.EmailService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterReqDTO req) throws RoleNotFoundException {

        return new ResponseEntity<>(authService.registerUser(req), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginReqDTO req) {
        LoginResDTO loginData = authService.loginUser(req);

        // 1. Crear la cookie de forma segura
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", loginData.jwt())
                .httpOnly(true)       // Evita que JavaScript (XSS) pueda leerla
                .secure(false)        // Ponlo en 'true' cuando subas a producci n con HTTPS
                .path("/")            // Disponible para todas las rutas
                .maxAge(86400)        // 1 d a, igual que la expiraci n de tu token
                .sameSite("Lax")      // Ayuda a prevenir CSRF. "Lax" funciona bien para localhost
                .build();

        // 2. Adjuntar la cookie a la respuesta
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(loginData);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cleanCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0) // maxAge 0 destruye la cookie inmediatamente
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                .body("Sesion cerrada exitosamente");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordReqDTO req) {
        try {
            return new ResponseEntity<>(authService.forgotPassword(req.username()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody VerifyCodeReqDTO req) {
        try {
            return new ResponseEntity<>(authService.verifyCode(req.username(), req.code()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordReqDTO req) {
        try {
            return new ResponseEntity<>(authService.resetPassword(req.username(), req.code(), req.newPassword()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/contact")
    public ResponseEntity<?> receiveContactForm(@Valid @RequestBody ContactReqDTO req) {
        try {
            emailService.sendContactEmail(req.nombre(), req.correo(), req.mascota(), req.mensaje());
            return new ResponseEntity<>("Mensaje enviado con éxito", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al enviar el mensaje", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/request-email-update")
    public ResponseEntity<?> requestEmailUpdate(
            @Valid @RequestBody RequestEmailUpdateReqDTO req,
            Authentication authentication) {
        try {
            // Extraemos el username seguro desde el token
            String realUsername = authentication.getName();
            return new ResponseEntity<>(authService.requestEmailUpdate(realUsername, req.newEmail()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/verify-email-update")
    public ResponseEntity<?> verifyEmailUpdate(
            @Valid @RequestBody VerifyEmailUpdateReqDTO req,
            Authentication authentication) {
        try {
            String realUsername = authentication.getName();
            return new ResponseEntity<>(authService.verifyAndApplyEmailUpdate(realUsername, req.code()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
