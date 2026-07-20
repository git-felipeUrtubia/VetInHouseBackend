package org.example.backend_vet_in_house.sales.repository;

import org.example.backend_vet_in_house.sales.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT ord FROM Orders ord WHERE ord.code = :code")
    Optional<Orders> findOrderByCode(@Param("code") String code);

    @Query("SELECT ord FROM Orders ord WHERE ord.userIdRef = :id")
    List<Orders> findOrderByUser(@Param("id") Long id);

    @Query("DELETE FROM Orders ord WHERE ord.code = :code")
    void cancelOrderByCode(@Param("code") String code);
}
