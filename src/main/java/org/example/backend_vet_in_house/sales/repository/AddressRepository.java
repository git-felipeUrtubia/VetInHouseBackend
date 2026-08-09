package org.example.backend_vet_in_house.sales.repository;

import org.example.backend_vet_in_house.sales.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
