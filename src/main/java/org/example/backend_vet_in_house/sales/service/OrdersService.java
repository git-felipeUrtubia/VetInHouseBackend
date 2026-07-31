package org.example.backend_vet_in_house.sales.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.catalog.model.Product;
import org.example.backend_vet_in_house.catalog.repository.ProductRepository;
import org.example.backend_vet_in_house.sales.dto.req.CreateOrderReqDTO;
import org.example.backend_vet_in_house.sales.dto.req.OrderDetailReqDTO;
import org.example.backend_vet_in_house.sales.dto.res.OrderDetailResDTO;
import org.example.backend_vet_in_house.sales.dto.res.OrderResDTO;
import org.example.backend_vet_in_house.sales.model.OrderStatus;
import org.example.backend_vet_in_house.sales.model.Orders;
import org.example.backend_vet_in_house.sales.repository.OrdersRepository;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductNotFoundException;
import org.example.backend_vet_in_house.shared.exception.sales.OrderAlreadyExistsException;
import org.example.backend_vet_in_house.shared.exception.user.UserNotFoundException;
import org.example.backend_vet_in_house.users.model.UserEntity;
import org.example.backend_vet_in_house.users.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final ProductRepository productRepository;
    private final OrderDetailService orderDetailService;
    private final OrdersRepository ordersRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public String createOrder(CreateOrderReqDTO req) {

        UserEntity user = userEntityRepository.findUserByUsername(req.username())
                .orElseThrow(() -> new UserNotFoundException("User " + req.username() + " not found"));
        boolean checkOrder = ordersRepository.findOrderByCode(req.code()).isPresent();


        if(checkOrder) {
            throw new OrderAlreadyExistsException("Order already exists");
        }

        Orders order = ordersRepository.save(Orders.builder()
                .code(req.code())
                .userIdRef(user.getUserId())
                .subtotal(req.subtotal())
                .tax(req.tax())
                .shippingCost(req.shippingCost())
                .totalAmount(req.totalAmount())
                .orderStatus(OrderStatus.valueOf(req.orderStatus()))
                .shippingAddress(req.shippingAddress())
                .createAt(req.createAt())
                .updateAt(req.updateAt())
                .paidAt(req.paidAt())
                .build()
        );

        List<OrderDetailReqDTO> listOrdersDetails = new ArrayList<>();

        req.orderDetails().forEach(od ->
            listOrdersDetails.add(new OrderDetailReqDTO(
                    od.codeProduct(),
                    od.quantity()
            ))
        );

        orderDetailService.createOrderDetail(listOrdersDetails, order);

        return "Order created with successfully";
    }

    public List<OrderResDTO> findAllOrders() {

        return ordersRepository.findAll().stream()
            .map(order -> {

                List<OrderDetailResDTO> odDTO = order.getOrdersDetails().stream().map(od -> {
                    Product prod = productRepository.findById(od.getProductIdRef())
                            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

                    return new OrderDetailResDTO(
                            prod.getName(),
                            od.getQuantity()
                    );
                }).toList();

                return new OrderResDTO(
                        order.getCode(),
                        order.getSubtotal(),
                        order.getTax(),
                        order.getShippingCost(),
                        order.getTotalAmount(),
                        order.getOrderStatus().name(),
                        order.getShippingAddress(),
                        order.getCreateAt(),
                        order.getUpdateAt(),
                        order.getPaidAt(),
                        odDTO
                );
            }
        ).toList();
    }
}
