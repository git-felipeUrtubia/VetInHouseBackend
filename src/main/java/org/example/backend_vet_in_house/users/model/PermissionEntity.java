package org.example.backend_vet_in_house.users.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permission")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long permissionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private PermissionEnum permissionEnum;

}
