package org.example.backend_vet_in_house.users.dto.req;

import jakarta.validation.constraints.NotBlank;

public record PhoneReqDTO(
        @NotBlank(message = "El número no puede estar vacío")
        String phoneNumber
) {}
