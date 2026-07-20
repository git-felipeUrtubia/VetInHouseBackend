package org.example.backend_vet_in_house.users.dto.res;

import java.util.List;

public record ContentOrderResDTO(
        String code,
        List<ItemsOrderResDTO> items
) {
}
