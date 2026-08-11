package org.example.backend_vet_in_house.sales.service;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.catalog.model.Product;
import org.example.backend_vet_in_house.catalog.repository.ProductRepository;
import org.example.backend_vet_in_house.sales.dto.req.OrderDetailReqDTO;
import org.example.backend_vet_in_house.sales.model.Orders;
import org.example.backend_vet_in_house.sales.model.OrdersDetail;
import org.example.backend_vet_in_house.sales.repository.OrdersDetailRepository;
import org.example.backend_vet_in_house.shared.exception.catalog.InsufficientStockException;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrdersDetailRepository ordersDetailRepository;
    private final ProductRepository productRepository;

    public void createOrderDetail(List<OrderDetailReqDTO> listOrdersDetails, Orders order) {

        List<OrdersDetail> res = listOrdersDetails.stream()
                .map(p -> {

                    Product prod = productRepository.findProductByCode(p.codeProduct())
                            .orElseThrow(() -> new ProductNotFoundException("Product " + p.codeProduct() + "not found"));

                    if(p.quantity() > prod.getStock()) {
                        throw new InsufficientStockException("Product " + p.codeProduct() + " stock insufficient");
                    }

                    prod.setStock(prod.getStock() - p.quantity());

                    return OrdersDetail.builder()
                            .productIdRef(prod.getProductId())
                            .productName(prod.getName())
                            .unitPrice(prod.getPrice())
                            .priceOffer(prod.getPriceOffer())
                            .quantity(p.quantity())
                            .order(order)
                            .build();

                }).toList();

        ordersDetailRepository.saveAll(res);

    }

}
