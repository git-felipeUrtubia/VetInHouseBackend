package org.example.backend_vet_in_house.users.dto.req;

import jakarta.validation.constraints.NotBlank;

public record LoginReqDTO(
        @NotBlank(message = "El email es obligatorio y no puede estar vacío")
        String username,
        @NotBlank(message = "La contraseña es obligatoria y no puede estar vacía")
        String password
) {
}
