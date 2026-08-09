package org.example.backend_vet_in_house.sales.service;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.sales.dto.req.CommuneReqDTO;
import org.example.backend_vet_in_house.sales.model.Commune;
import org.example.backend_vet_in_house.sales.repository.CommuneRepository;
import org.example.backend_vet_in_house.shared.exception.sales.CommuneNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommuneService {

    private final CommuneRepository communeRepository;

    public Commune saveCommune(CommuneReqDTO req) {

        return communeRepository.save(Commune.builder()
                        .code(req.code())
                        .commune(req.commune())
                .build());
    }

    public Commune findCommuneByCode(String code) {

        return communeRepository.findCommuneByCode(code)
                .orElseThrow(() -> new CommuneNotFoundException("Commune " + code + " not found"));
    }


}
