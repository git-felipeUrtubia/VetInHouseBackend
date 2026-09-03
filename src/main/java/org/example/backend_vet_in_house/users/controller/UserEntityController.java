package org.example.backend_vet_in_house.users.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.users.service.UserEntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserEntityController {

    private final UserEntityService userEntityService;

    @GetMapping("/find/order-history")
    public ResponseEntity<?> getOrderHistory(Authentication authentication) {
        String username = authentication.getName();
        return new ResponseEntity<>(userEntityService.getOrderHistoryByUser(username), HttpStatus.OK);
    }

    @GetMapping("/find/pet-appointment")
    public ResponseEntity<?> getAppointmentByUsername(Authentication authentication) {
        String username = authentication.getName();
        return new ResponseEntity<>(userEntityService.getAppointmentByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/find/pet-user")
    public ResponseEntity<?> getPetByUsername(Authentication authentication) {
        String username = authentication.getName();
        return new ResponseEntity<>(userEntityService.getPetByUsername(username), HttpStatus.OK);
    }

}
