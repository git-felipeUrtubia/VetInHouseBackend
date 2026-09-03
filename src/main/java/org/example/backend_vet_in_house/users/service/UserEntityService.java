package org.example.backend_vet_in_house.users.service;


import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.dto.res.AppointmentResultResDTO;
import org.example.backend_vet_in_house.appointments.model.Appointment;
import org.example.backend_vet_in_house.appointments.repository.AppointmentRepository;
import org.example.backend_vet_in_house.pets.model.Pet;
import org.example.backend_vet_in_house.pets.repository.PetRepository;
import org.example.backend_vet_in_house.sales.model.Orders;
import org.example.backend_vet_in_house.sales.repository.OrdersRepository;
import org.example.backend_vet_in_house.shared.exception.user.UserNotFoundException;
import org.example.backend_vet_in_house.users.dto.res.*;
import org.example.backend_vet_in_house.users.model.UserEntity;
import org.example.backend_vet_in_house.users.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;
    private final OrdersRepository ordersRepository;

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;

    public OrderHistoryResDTO getOrderHistoryByUser(String username) {

        UserEntity user = userEntityRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        List<Orders> orders = ordersRepository.findOrderByUser(user.getUserId());

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

        // 1. Obtener las mascotas y sus IDs
        List<Pet> pets = petRepository.findPetsByUserId(user.getUserId());
        List<Long> petIds = pets.stream().map(Pet::getPetId).toList();

        // 2. Obtener todas las citas + resultados en UNA sola consulta
        List<Appointment> allAppointments = petIds.isEmpty() ?
                List.of() :
                appointmentRepository.findAllByPetIdsWithResults(petIds);

        // 3. Agrupar las citas por el ID de la mascota en memoria
        Map<Long, List<Appointment>> appointmentsByPetId = allAppointments.stream()
                .collect(Collectors.groupingBy(Appointment::getPetIdRef));

        // 4. Mapear a DTOs sin hacer nuevas consultas a BD
        List<ItemPetFromUserResDTO> itemsPet = pets.stream().map(pet -> {
            List<Appointment> petAppointments = appointmentsByPetId.getOrDefault(pet.getPetId(), List.of());

            List<ItemAppointmentFromUserResDTO> itemsAp = petAppointments.stream().map(it -> {
                AppointmentResultResDTO resultDto = it.getAppointmentResult() != null ?
                        new AppointmentResultResDTO(
                                it.getCodeService(),
                                it.getAppointmentResult().getDiagnosis(),
                                it.getAppointmentResult().getTreatment(),
                                it.getAppointmentResult().getCreatedAt()
                        ) : null;

                return new ItemAppointmentFromUserResDTO(
                        it.getCodeService(),
                        it.getServiceType().name(),
                        it.getAppointmentDate(),
                        it.getCreateAt(),
                        it.getStatus().name(),
                        resultDto
                );
            }).toList();

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
