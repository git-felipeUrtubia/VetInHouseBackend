package org.example.backend_vet_in_house.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.users.dto.req.*;
import org.example.backend_vet_in_house.users.dto.res.LoginResDTO;
import org.example.backend_vet_in_house.users.service.AuthService;
import org.example.backend_vet_in_house.users.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        return new ResponseEntity<>(authService.loginUser(req), HttpStatus.OK);
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
    public ResponseEntity<?> requestEmailUpdate(@Valid @RequestBody RequestEmailUpdateReqDTO req) {
        try {
            return new ResponseEntity<>(authService.requestEmailUpdate(req.currentEmail(), req.newEmail()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/verify-email-update")
    public ResponseEntity<?> verifyEmailUpdate(@Valid @RequestBody VerifyEmailUpdateReqDTO req) {
        try {
            return new ResponseEntity<>(authService.verifyAndApplyEmailUpdate(req.currentEmail(), req.code()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
