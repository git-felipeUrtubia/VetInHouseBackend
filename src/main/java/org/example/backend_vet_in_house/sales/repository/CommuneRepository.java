package org.example.backend_vet_in_house.sales.repository;

import org.example.backend_vet_in_house.sales.model.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, Long> {

    @Query("SELECT c FROM Commune c WHERE c.code = :code")
    Optional<Commune> findCommuneByCode(@Param("code") String code);

}
