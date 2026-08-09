package org.example.backend_vet_in_house.sales.service;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.sales.dto.req.AddressReqDTO;
import org.example.backend_vet_in_house.sales.model.Address;
import org.example.backend_vet_in_house.sales.model.Commune;
import org.example.backend_vet_in_house.sales.model.Region;
import org.example.backend_vet_in_house.sales.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public Address saveAddress(AddressReqDTO req, Commune commune, Region region) {

        commune.setRegion(region);

        return addressRepository.save(Address.builder()
                        .code(req.code())
                        .street(req.street())
                        .number(req.number())
                        .commune(commune)
                .build());

    }

}
