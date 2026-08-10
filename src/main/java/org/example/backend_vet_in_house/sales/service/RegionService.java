package org.example.backend_vet_in_house.sales.service;


import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.sales.dto.req.RegionReqDTO;
import org.example.backend_vet_in_house.sales.dto.res.RegionResDTO;
import org.example.backend_vet_in_house.sales.model.Region;
import org.example.backend_vet_in_house.sales.repository.RegionRepository;
import org.example.backend_vet_in_house.shared.exception.sales.RegionNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public Region saveRegion(RegionReqDTO req) {

        return regionRepository.save(Region.builder()
                        .code(req.code())
                        .region(req.region())
                        .shippingCost(req.shippingCost())
                .build());

    }

    public Region findRegionByCode(String code) {

        return regionRepository.findRegionByCode(code)
                .orElseThrow(() -> new RegionNotFoundException("Region not found"));

    }

}
