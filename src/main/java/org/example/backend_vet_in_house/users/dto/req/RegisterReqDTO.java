package org.example.backend_vet_in_house.users.dto.req;

import jakarta.validation.constraints.NotBlank;

public record RegisterReqDTO(
        @NotBlank(message = "El nombre es obligatorio y no puede estar vacío")
        String firstName,
        @NotBlank(message = "El apellido es obligatorio y no puede estar vacío")
        String lastName,
        @NotBlank(message = "El email es obligatorio y no puede estar vacío")
        String username,
        @NotBlank(message = "La contraseña es obligatorio y no puede estar vacío")
        String password
) {
}
