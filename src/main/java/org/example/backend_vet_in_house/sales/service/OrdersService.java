package org.example.backend_vet_in_house.sales.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.catalog.model.Product;
import org.example.backend_vet_in_house.catalog.repository.ProductRepository;
import org.example.backend_vet_in_house.sales.dto.req.CreateOrderReqDTO;
import org.example.backend_vet_in_house.sales.dto.req.OrderDetailReqDTO;
import org.example.backend_vet_in_house.sales.dto.req.OrderTotals;
import org.example.backend_vet_in_house.sales.dto.res.*;
import org.example.backend_vet_in_house.sales.model.*;
import org.example.backend_vet_in_house.sales.repository.OrdersRepository;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductNotFoundException;
import org.example.backend_vet_in_house.shared.exception.sales.OrderAlreadyExistsException;
import org.example.backend_vet_in_house.shared.exception.user.UserNotFoundException;
import org.example.backend_vet_in_house.users.model.UserEntity;
import org.example.backend_vet_in_house.users.repository.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final ProductRepository productRepository;
    private final OrderDetailService orderDetailService;
    private final OrdersRepository ordersRepository;
    private final UserEntityRepository userEntityRepository;
    private final AddressService addressService;
    private final CommuneService communeService;
    private final RegionService regionService;

    @Transactional
    public String createOrder(CreateOrderReqDTO req) {

        UserEntity user = userEntityRepository.findUserByUsername(req.username())
                .orElseThrow(() -> new UserNotFoundException("User " + req.username() + " not found"));
        boolean checkOrder = ordersRepository.findOrderByCode(req.code()).isPresent();


        if(checkOrder) {
            throw new OrderAlreadyExistsException("Order already exists");
        }

        Region region = regionService.findRegionByCode(req.codeRegion());
        Commune commune = communeService.findCommuneByCode(req.codeCommune());
        Address address = addressService.saveAddress(req.address(), commune, region);


        BigDecimal subtotal = req.orderDetails().stream()
                .map(od -> {

                    Product p = productRepository.findProductByCode(od.codeProduct())
                            .orElseThrow(() -> new ProductNotFoundException(
                                    "Product " + od.codeProduct() + " not found"
                            ));

                    BigDecimal quantity = BigDecimal.valueOf(od.quantity());
                    BigDecimal total = p.getPrice().multiply(quantity);
                    BigDecimal descTotal = p.getPriceOffer().multiply(quantity);

                    return total.subtract(descTotal);

                }).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingCost = region.getShippingCost();
        BigDecimal totalAmount = subtotal.add(shippingCost);

        BigDecimal ivaFactor = new BigDecimal("1.19");
        BigDecimal valueNeto = totalAmount.divide(ivaFactor, 0, RoundingMode.HALF_UP);
        BigDecimal tax = totalAmount.subtract(valueNeto);

        OrderTotals orderTotals = new OrderTotals(
                valueNeto,
                tax,
                shippingCost,
                totalAmount
        );




        Orders order = ordersRepository.save(Orders.builder()
                .code(req.code())
                .userIdRef(user.getUserId())
                .subtotal( orderTotals.subtotal() )
                .tax( orderTotals.tax() )
                .shippingCost( orderTotals.shippingCost() )
                .totalAmount( orderTotals.totalAmount() )
                .orderStatus(OrderStatus.valueOf(req.orderStatus()))
                .createAt(req.createAt())
                .updateAt(req.updateAt())
                .paidAt(req.paidAt())
                .address(address)
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

                AddressResDTO address = new AddressResDTO(
                        order.getAddress().getCode(),
                        order.getAddress().getStreet(),
                        order.getAddress().getNumber()
                );
                CommuneResDTO commune = new CommuneResDTO(
                        order.getAddress().getCommune().getCode(),
                        order.getAddress().getCommune().getCommune()
                );
                RegionResDTO region = new RegionResDTO(
                        order.getAddress().getCommune().getRegion().getCode(),
                        order.getAddress().getCommune().getRegion().getRegion(),
                        order.getAddress().getCommune().getRegion().getShippingCost()
                );

                return new OrderResDTO(
                        order.getCode(),
                        order.getSubtotal(),
                        order.getTax(),
                        order.getShippingCost(),
                        order.getTotalAmount(),
                        order.getOrderStatus().name(),
                        order.getCreateAt(),
                        order.getUpdateAt(),
                        order.getPaidAt(),
                        odDTO,
                        address,
                        commune,
                        region
                );
            }
        ).toList();
    }
}
