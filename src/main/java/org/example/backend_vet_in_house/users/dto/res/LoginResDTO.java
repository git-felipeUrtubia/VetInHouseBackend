package org.example.backend_vet_in_house.users.dto.res;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({
        "firstName",
        "lastName",
        "username",
        "message",
        "status"
})
public record LoginResDTO(
        String firstName,
        String lastName,
        String username,
        String message,
        @JsonIgnore // Esto impide que el JWT se env e en el JSON
        String jwt,
        List<RoleResDTO> roles,
        boolean status
) {
}
