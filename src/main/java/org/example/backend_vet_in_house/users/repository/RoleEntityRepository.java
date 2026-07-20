package org.example.backend_vet_in_house.users.repository;

import org.example.backend_vet_in_house.users.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {
}
