package org.example.backend_vet_in_house.sales.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.catalog.model.Product;
import org.example.backend_vet_in_house.catalog.repository.ProductRepository;
import org.example.backend_vet_in_house.sales.dto.req.OrderDetailReqDTO;
import org.example.backend_vet_in_house.sales.model.Orders;
import org.example.backend_vet_in_house.sales.model.OrdersDetail;
import org.example.backend_vet_in_house.sales.repository.OrdersDetailRepository;
import org.example.backend_vet_in_house.sales.repository.OrdersRepository;
import org.example.backend_vet_in_house.shared.exception.catalog.InsufficientStockException;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductNotFoundException;
import org.example.backend_vet_in_house.shared.exception.sales.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrdersDetailRepository ordersDetailRepository;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

    public void createOrderDetail(OrderDetailReqDTO req, String code) {

        Orders order = ordersRepository.findOrderByCode(code)
                        .orElseThrow(() -> new OrderNotFoundException("Order " + code + " no encontrado"));

        Product prod = productRepository.findProductByCode(req.codeProduct())
                        .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if(prod.getStock() < req.quantity()) {
            throw new InsufficientStockException("Stock insufficient");
        }

        prod.setStock(prod.getStock() - req.quantity());

        ordersDetailRepository.save(OrdersDetail.builder()
                        .productIdRef(prod.getProductId())
                        .quantity(req.quantity())
                        .order(order)
                .build()
        );

    }

}
