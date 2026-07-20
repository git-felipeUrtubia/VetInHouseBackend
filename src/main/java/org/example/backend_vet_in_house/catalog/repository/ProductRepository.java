package org.example.backend_vet_in_house.catalog.repository;

import jakarta.transaction.Transactional;
import org.example.backend_vet_in_house.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.code = :code")
    Optional<Product> findProductByCode(@Param("code") String code);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.code = :code")
    void deleteProductByCode(@Param("code") String code);
}
