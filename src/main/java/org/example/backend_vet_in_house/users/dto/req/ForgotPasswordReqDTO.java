package org.example.backend_vet_in_house.users.dto.req;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordReqDTO(
        @NotBlank(message = "El email es obligatorio") String username
) {
}
