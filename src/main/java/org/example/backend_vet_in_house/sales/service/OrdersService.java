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
import org.example.backend_vet_in_house.shared.exception.sales.OrderNotFoundException;
import org.example.backend_vet_in_house.shared.exception.shipping.CommuneNotBelongToRegion;
import org.example.backend_vet_in_house.shared.exception.user.UserNotFoundException;
import org.example.backend_vet_in_house.users.model.UserEntity;
import org.example.backend_vet_in_house.users.repository.UserEntityRepository;
import org.example.backend_vet_in_house.users.service.UserEntityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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

    @Value("${spring.iva.factor}")
    private float iva_factor;

    @Transactional
    public String createOrder(CreateOrderReqDTO req, String username) {

        UserEntity user = userEntityRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));
        boolean checkOrder = ordersRepository.findOrderByCode(req.code()).isPresent();


        if(checkOrder) {
            throw new OrderAlreadyExistsException("Order already exists");
        }

        Region region = regionService.findRegionByCode(req.codeRegion());
        Commune commune = communeService.findCommuneByCode(req.codeCommune());

        if(!region.getRegionId().equals(commune.getRegion().getRegionId())) {
            throw new CommuneNotBelongToRegion("the commune not belong to region");
        }

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

        System.out.println("SUBTOTAL: " + subtotal);
        BigDecimal shippingCost = region.getShippingCost();
        BigDecimal totalAmount = subtotal.add(shippingCost);

        BigDecimal ivaFactor = new BigDecimal(iva_factor);
        BigDecimal valueNeto = totalAmount.divide(ivaFactor, 0, RoundingMode.HALF_UP);
        BigDecimal tax = totalAmount.subtract(valueNeto);

        OrderTotals orderTotals = new OrderTotals(
                subtotal,
                tax,
                shippingCost,
                totalAmount
        );

        Orders order = ordersRepository.save(Orders.builder()
                .code(req.code())
                .phone(req.phone())
                .userIdRef(user.getUserId())
                .subtotal( orderTotals.subtotal() )
                .tax( orderTotals.tax() )
                .shippingCost( orderTotals.shippingCost() )
                .totalAmount( orderTotals.totalAmount() )
                .orderStatus(OrderStatus.PENDING)
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

    @Transactional
    public List<OrderResDTO> findAllOrders() {

        return ordersRepository.findAll().stream()
            .map(order -> {

                List<OrderDetailResDTO> odDTO = order.getOrdersDetails().stream().map(od -> {

                    return new OrderDetailResDTO(
                            od.getProductName(),
                            od.getUnitPrice(),
                            od.getPriceOffer(),
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

                UserEntity user = userEntityRepository.findById(order.getUserIdRef())
                        .orElseThrow(() -> new UserNotFoundException("User " + order.getUserIdRef() + " not found"));

                return new OrderResDTO(
                        order.getCode(),
                        order.getPhone(),
                        user.getUsername(),
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

    @Transactional
    public OrderResDTO findOrderByCode(String code) {
        Orders order = ordersRepository.findOrderByCode(code)
                .orElseThrow(() -> new OrderNotFoundException("Order " + code + " not found"));

        List<OrderDetailResDTO> orderDetails = order.getOrdersDetails().stream()
                .map(od -> new OrderDetailResDTO(
                        od.getProductName(),
                        od.getUnitPrice(),
                        od.getPriceOffer(),
                        od.getQuantity()
                )).toList();

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

        UserEntity user = userEntityRepository.findById(order.getUserIdRef())
                .orElseThrow(() -> new UserNotFoundException("User " + order.getUserIdRef() + " not found"));

        return new OrderResDTO(
                order.getCode(),
                order.getPhone(),
                user.getUsername(),
                order.getSubtotal(),
                order.getTax(),
                order.getShippingCost(),
                order.getTotalAmount(),
                order.getOrderStatus().name(),
                order.getCreateAt(),
                order.getUpdateAt(),
                order.getPaidAt(),
                orderDetails,
                address,
                commune,
                region
        );
    }

    @Transactional
    public OrderResDTO updateOrderStatus(String code, OrderStatus newStatus) {
        Orders order = ordersRepository.findOrderByCode(code)
                .orElseThrow(() -> new OrderNotFoundException("Order " + code + " not found"));

        order.setOrderStatus( newStatus );
        ordersRepository.save(order);

        return findOrderByCode(code);
    }

    @Scheduled(fixedRate = 300000) // Se ejecuta automáticamente cada 5 minutos (300,000 ms)
    @Transactional
    public void cancelExpiredOrdersAndRestoreStock() {
        // Calculamos el tiempo límite: Órdenes creadas hace más de 15 minutos
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(15);

        List<Orders> expiredOrders = ordersRepository.findExpiredOrders(OrderStatus.PENDING, expirationTime);

        for (Orders order : expiredOrders) {
            // Cambiar a cancelado para que Transbank o el frontend lo detecten si intentan retomarlo
            order.setOrderStatus(OrderStatus.CANCELLED);

            // Restituir el stock sumando la cantidad reservada
            for (OrdersDetail detail : order.getOrdersDetails()) {
                Product product = productRepository.findById(detail.getProductIdRef())
                        .orElseThrow(() -> new ProductNotFoundException("Product not found during stock restore"));

                product.setStock(product.getStock() + detail.getQuantity());
                productRepository.save(product);
            }

            ordersRepository.save(order);
            System.out.println("Orden expirada " + order.getCode() + " cancelada. Stock restituido.");
        }
    }
}











