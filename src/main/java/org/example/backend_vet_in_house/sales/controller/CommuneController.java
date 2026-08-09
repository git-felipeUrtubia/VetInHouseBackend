package org.example.backend_vet_in_house.sales.controller;


import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.sales.dto.req.AddressReqDTO;
import org.example.backend_vet_in_house.sales.dto.req.CommuneReqDTO;
import org.example.backend_vet_in_house.sales.service.CommuneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/commune")
@RequiredArgsConstructor
public class CommuneController {

    private final CommuneService communeService;

    @PostMapping("/create")
    public ResponseEntity<?> saveCommune(@RequestBody CommuneReqDTO req) {
        return new ResponseEntity<>(communeService.saveCommune(req), HttpStatus.CREATED);
    }

}
