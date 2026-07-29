package org.example.backend_vet_in_house.sales.repository;

import org.example.backend_vet_in_house.sales.model.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long> {

}
