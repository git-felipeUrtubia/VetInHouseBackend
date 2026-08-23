package org.example.backend_vet_in_house.users.dto.req;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordReqDTO(
        @NotBlank(message = "El email es obligatorio") String username,
        @NotBlank(message = "El código es obligatorio") String code,
        @NotBlank(message = "La nueva contraseña es obligatoria") String newPassword
) {
}
