package org.example.backend_vet_in_house.sales.repository;

import org.example.backend_vet_in_house.sales.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("SELECT r FROM Region r WHERE r.code = :code")
    Optional<Region> findRegionByCode(@Param("code") String code);

}
