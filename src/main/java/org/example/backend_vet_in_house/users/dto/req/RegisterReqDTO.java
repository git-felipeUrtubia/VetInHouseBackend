package org.example.backend_vet_in_house.users.dto.req;

public record RegisterReqDTO(
        String firstName,
        String lastName,
        String username,
        String password
) {
}
