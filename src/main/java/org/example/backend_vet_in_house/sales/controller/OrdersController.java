package org.example.backend_vet_in_house.sales.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.sales.dto.req.CreateOrderReqDTO;
import org.example.backend_vet_in_house.sales.service.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderReqDTO req) {
        return new ResponseEntity<>(ordersService.createOrder(req), HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(ordersService.findAllOrders(), HttpStatus.OK);
    }

}
