package org.example.backend_vet_in_house.appointments.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.dto.req.CreateAppointmentReqDTO;
import org.example.backend_vet_in_house.appointments.dto.res.AppointmentResDTO;
import org.example.backend_vet_in_house.appointments.model.Appointment;
import org.example.backend_vet_in_house.appointments.model.ServiceType;
import org.example.backend_vet_in_house.appointments.model.Status;
import org.example.backend_vet_in_house.appointments.repository.AppointmentRepository;
import org.example.backend_vet_in_house.pets.model.Pet;
import org.example.backend_vet_in_house.pets.repository.PetRepository;
import org.example.backend_vet_in_house.shared.exception.appointment.AppointmentAlreadyExistException;
import org.example.backend_vet_in_house.shared.exception.pet.PetNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;

    public String createAppointment(CreateAppointmentReqDTO req) {

        Pet pet = petRepository.findPetByPatientNumber(req.patientNumber())
                .orElseThrow(() -> new PetNotFoundException("Pet " + req.patientNumber() + " not found"));

        boolean checkAppointment = appointmentRepository.findAppointmentByCode(req.codeService()).isPresent();

        if(checkAppointment) {
            throw new AppointmentAlreadyExistException("This Appointment Already exist");
        }

        appointmentRepository.save(
                Appointment.builder()
                        .petIdRef(pet.getPetId())
                        .codeService(req.codeService())
                        .reasonForVisit(req.reasonForVisit())
                        .appointmentDate(req.appointmentDate())
                        .createAt(req.createAt())
                        .updateAt(req.updateAt())
                        .serviceType(ServiceType.valueOf(req.serviceType()))
                        .status(Status.valueOf(req.status()))
                        .build()
        );

        return "Appointment create with successfully!";
    }

    @Transactional
    public List<AppointmentResDTO> findAllAppointment() {

        return appointmentRepository.findAll().stream()
                .map(ap -> {

                    Pet pet = petRepository.findById(ap.getPetIdRef())
                            .orElseThrow(() -> new PetNotFoundException("Pet " + ap.getPetIdRef() + "not found"));

                    return new AppointmentResDTO(
                            pet.getPatientNumber(),
                            pet.getName(),
                            pet.getWeight(),
                            pet.getAge(),
                            ap.getCodeService(),
                            ap.getReasonForVisit(),
                            ap.getAppointmentDate(),
                            ap.getCreateAt(),
                            ap.getUpdateAt(),
                            ap.getServiceType().name(),
                            ap.getStatus().name()
                    );

                }).toList();

    }

}
