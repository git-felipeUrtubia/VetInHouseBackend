package org.example.backend_vet_in_house.users.service;


import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.repository.AppointmentRepository;
import org.example.backend_vet_in_house.catalog.model.Product;
import org.example.backend_vet_in_house.catalog.repository.ProductRepository;
import org.example.backend_vet_in_house.pets.model.Pet;
import org.example.backend_vet_in_house.pets.repository.PetRepository;
import org.example.backend_vet_in_house.sales.model.Orders;
import org.example.backend_vet_in_house.sales.repository.OrdersRepository;
import org.example.backend_vet_in_house.shared.exception.catalog.ProductNotFoundException;
import org.example.backend_vet_in_house.shared.exception.sales.OrderByUserIdNotFoundException;
import org.example.backend_vet_in_house.shared.exception.user.UserNotFoundException;
import org.example.backend_vet_in_house.users.dto.res.*;
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

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;

    public OrderHistoryResDTO getOrderHistoryByUser(String username) {

        UserEntity user = userEntityRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        List<Orders> orders = ordersRepository.findOrderByUser(user.getUserId());

        if(orders.isEmpty()) {
            throw new OrderByUserIdNotFoundException("User have not orders");
        }

        List<ContentOrderResDTO> contentsOrder = orders.stream().map(order -> {

            List<ItemsOrderResDTO> items = order.getOrdersDetails().stream().map(od -> {

                return new ItemsOrderResDTO(
                        od.getProductName(),
                        od.getUnitPrice(),
                        od.getPriceOffer(),
                        od.getQuantity()
                );

            }).toList();

            return new ContentOrderResDTO(
                    order.getCode(),
                    order.getSubtotal(),
                    order.getShippingCost(),
                    order.getTax(),
                    order.getTotalAmount(),
                    items
            );
        }).toList();

        return new OrderHistoryResDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                contentsOrder
        );

    }

    public AppointmentFromUserResDTO getAppointmentByUsername(String username) {

        UserEntity user = userEntityRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));

        List<Pet> pets = petRepository.findPetsByUserId(user.getUserId());

        List<ItemPetFromUserResDTO> itemsPet = pets.stream()
                .map(pet -> {

                    List<ItemAppointmentFromUserResDTO> itemsAp = appointmentRepository
                            .findAllByPet(pet.getPetId()).stream()
                            .map(it -> new ItemAppointmentFromUserResDTO(
                                    it.getCodeService(),
                                    it.getServiceType().name(),
                                    it.getAppointmentDate(),
                                    it.getCreateAt(),
                                    it.getStatus().name()
                            )).toList();

                    return new ItemPetFromUserResDTO(
                            pet.getPatientNumber(),
                            pet.getName(),
                            itemsAp
                    );

                }).toList();

        return new AppointmentFromUserResDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                itemsPet
        );
    }

    public PetFromUserResDTO getPetByUsername(String username) {

        UserEntity user = userEntityRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));

        List<Pet> pets = petRepository.findPetsByUserId(user.getUserId());

        List<ItemPetUser> items = pets.stream()
                .map(p -> new ItemPetUser(
                    p.getPatientNumber(),
                    p.getName(),
                    p.getAge(),
                    p.getWeight(),
                    p.getSpecie().name(),
                    p.getGender().name(),
                    p.getBreed(),
                    p.isNeutered(),
                    p.getAllergies(),
                    p.getMicrochipNumber()
                )
        ).toList();

        return new PetFromUserResDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                items
        );

    }
}
