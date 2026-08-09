package org.example.backend_vet_in_house.sales.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.sales.service.AddressService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;



}
