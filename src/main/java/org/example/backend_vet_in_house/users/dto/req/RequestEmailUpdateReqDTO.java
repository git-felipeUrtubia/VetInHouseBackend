package org.example.backend_vet_in_house.users.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestEmailUpdateReqDTO(
        @NotBlank(message = "El correo actual es obligatorio") String currentEmail,
        @Email(message = "Formato inválido") @NotBlank(message = "El nuevo correo es obligatorio") String newEmail
) {
}
