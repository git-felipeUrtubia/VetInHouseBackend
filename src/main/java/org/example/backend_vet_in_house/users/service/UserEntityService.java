package org.example.backend_vet_in_house.users.service;


import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.catalog.model.Product;
import org.example.backend_vet_in_house.catalog.repository.ProductRepository;
import org.example.backend_vet_in_house.sales.model.Orders;
import org.example.backend_vet_in_house.sales.repository.OrdersRepository;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductNotFoundException;
import org.example.backend_vet_in_house.shared.exception.sales.OrderByUserIdNotFoundException;
import org.example.backend_vet_in_house.users.dto.res.ContentOrderResDTO;
import org.example.backend_vet_in_house.users.dto.res.ItemsOrderResDTO;
import org.example.backend_vet_in_house.users.dto.res.OrderHistoryResDTO;
import org.example.backend_vet_in_house.users.model.UserEntity;
import org.example.backend_vet_in_house.users.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

    public OrderHistoryResDTO getOrderHistoryByUser(Long id) {

        UserEntity user = userEntityRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Orders> orders = ordersRepository.findOrderByUser(user.getUserId());

        if(orders.isEmpty()) {
            throw new OrderByUserIdNotFoundException("User have not orders");
        }

        List<ContentOrderResDTO> contentsOrder = orders.stream().map(order -> {

            List<ItemsOrderResDTO> items = order.getOrdersDetails().stream().map(od -> {

                Product prod = productRepository.findById(od.getProductIdRef())
                        .orElseThrow(() -> new ProductNotFoundException("Product not found"));

                return new ItemsOrderResDTO(prod.getName(), od.getQuantity());

            }).toList();

            return new ContentOrderResDTO(order.getCode(), items);
        }).toList();

        return new OrderHistoryResDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                contentsOrder
        );

    }

}
