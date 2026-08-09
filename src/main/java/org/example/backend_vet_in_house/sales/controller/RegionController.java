package org.example.backend_vet_in_house.sales.controller;


import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.sales.dto.req.AddressReqDTO;
import org.example.backend_vet_in_house.sales.dto.req.CommuneReqDTO;
import org.example.backend_vet_in_house.sales.dto.req.RegionReqDTO;
import org.example.backend_vet_in_house.sales.service.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/create")
    public ResponseEntity<?> saveAddress(@RequestBody RegionReqDTO req) {
        return new ResponseEntity<>(regionService.saveRegion(req), HttpStatus.CREATED);
    }

}
