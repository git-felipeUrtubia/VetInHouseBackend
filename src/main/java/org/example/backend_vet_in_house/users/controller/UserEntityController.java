package org.example.backend_vet_in_house.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.users.dto.req.PhoneReqDTO;
import org.example.backend_vet_in_house.users.dto.res.PhoneResDTO;
import org.example.backend_vet_in_house.users.service.UserEntityService;
import org.example.backend_vet_in_house.users.service.UserPhoneEntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserEntityController {

    private final UserEntityService userEntityService;
    private final UserPhoneEntityService userPhoneEntityService;

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

    @PostMapping("/save/phone")
    public ResponseEntity<PhoneResDTO> addPhone(@Valid @RequestBody PhoneReqDTO req, Authentication authentication) {
        String username = authentication.getName();

        PhoneResDTO response = userPhoneEntityService.addPhone(username, req);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/find/phones")
    public ResponseEntity<?> getPhonesByUser(Authentication authentication) {
        String username = authentication.getName();
        return new ResponseEntity<>(userPhoneEntityService.findPhoneByUser(username), HttpStatus.OK);
    }

    @DeleteMapping("/delete/phone")
    public ResponseEntity<?> deletePhoneById(Authentication authentication, @RequestParam Long id) {
        String username = authentication.getName();
        userPhoneEntityService.deletePhoneById(username, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
