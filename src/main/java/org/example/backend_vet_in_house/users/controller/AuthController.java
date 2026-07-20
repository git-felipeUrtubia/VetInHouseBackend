package org.example.backend_vet_in_house.users.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.users.dto.req.LoginReqDTO;
import org.example.backend_vet_in_house.users.dto.req.RegisterReqDTO;
import org.example.backend_vet_in_house.users.dto.res.LoginResDTO;
import org.example.backend_vet_in_house.users.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterReqDTO req) throws RoleNotFoundException {

        return new ResponseEntity<>(authService.registerUser(req), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginReqDTO req) {

        return new ResponseEntity<>(authService.loginUser(req), HttpStatus.OK);
    }


}
