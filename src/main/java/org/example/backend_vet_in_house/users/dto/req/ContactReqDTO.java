package org.example.backend_vet_in_house.users.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactReqDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @Email(message = "Formato de correo inválido") @NotBlank(message = "El correo es obligatorio") String correo,
        String mascota,
        @NotBlank(message = "El mensaje es obligatorio") String mensaje
) {
}
