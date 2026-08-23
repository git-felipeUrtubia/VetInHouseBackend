package org.example.backend_vet_in_house.users.dto.req;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailUpdateReqDTO(
        @NotBlank(message = "El correo actual es obligatorio") String currentEmail,
        @NotBlank(message = "El código es obligatorio") String code
) {
}
