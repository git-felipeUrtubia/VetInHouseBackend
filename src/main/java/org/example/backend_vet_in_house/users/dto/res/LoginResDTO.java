package org.example.backend_vet_in_house.users.dto.res;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "username",
        "message",
        "jwt",
        "status"
})
public record LoginResDTO(
        String username,
        String message,
        String jwt,
        boolean status
) {
}
