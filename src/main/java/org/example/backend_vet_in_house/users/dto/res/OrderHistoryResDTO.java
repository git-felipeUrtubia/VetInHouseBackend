package org.example.backend_vet_in_house.users.dto.res;

import java.util.List;

public record OrderHistoryResDTO(
        String firstName,
        String lastName,
        String username,
        List<ContentOrderResDTO> orders
) {

}
