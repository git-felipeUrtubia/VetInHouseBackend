package org.example.backend_vet_in_house.appointments.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.dto.req.CreateAppointmentReqDTO;
import org.example.backend_vet_in_house.appointments.dto.req.UpdateAppointmentReqDTO;
import org.example.backend_vet_in_house.appointments.dto.res.AppointmentResDTO;
import org.example.backend_vet_in_house.appointments.model.Appointment;
import org.example.backend_vet_in_house.appointments.model.AvailableSlot;
import org.example.backend_vet_in_house.appointments.model.ServiceType;
import org.example.backend_vet_in_house.appointments.model.Status;
import org.example.backend_vet_in_house.appointments.repository.AppointmentRepository;
import org.example.backend_vet_in_house.appointments.repository.AvailableSlotRepository;
import org.example.backend_vet_in_house.pets.model.Pet;
import org.example.backend_vet_in_house.pets.repository.PetRepository;
import org.example.backend_vet_in_house.shared.exception.appointment.AppointmentAlreadyExistException;
import org.example.backend_vet_in_house.shared.exception.appointment.AppointmentNotFoundException;
import org.example.backend_vet_in_house.shared.exception.pet.PetNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final AvailableSlotRepository availableSlotRepository;

    @Transactional
    public String createAppointment(CreateAppointmentReqDTO req) {
        Pet pet = petRepository.findPetByPatientNumber(req.patientNumber())
                .orElseThrow(() -> new PetNotFoundException("Pet " + req.patientNumber() + " not found"));

        boolean checkAppointment = appointmentRepository.findAppointmentByCode(req.codeService()).isPresent();
        if(checkAppointment) {
            throw new AppointmentAlreadyExistException("This Appointment Already exist");
        }

        // --- Bloqueamos el horario ---
        bookSlot(req.appointmentDate());

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
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) return List.of();

        List<Long> petIds = appointments.stream()
                .map(Appointment::getPetIdRef)
                .distinct()
                .toList();

        Map<Long, Pet> petMap = petRepository.findAllById(petIds).stream()
                .collect(Collectors.toMap(Pet::getPetId, pet -> pet));

        return appointments.stream()
                .map(ap -> {
                    Pet pet = petMap.get(ap.getPetIdRef());
                    if (pet == null) {
                        throw new PetNotFoundException("Pet " + ap.getPetIdRef() + " not found");
                    }
                    return buildResDTO(ap, pet);
                }).toList();
    }

    public AppointmentResDTO findAppointmentByCode(String codeService) {
        Appointment ap = appointmentRepository.findAppointmentByCode(codeService)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment " + codeService + " not found"));
        Pet pet = petRepository.findById(ap.getPetIdRef())
                .orElseThrow(() -> new PetNotFoundException("Pet " + ap.getPetIdRef() + " not found"));

        return buildResDTO(ap, pet);
    }

    @Transactional
    public AppointmentResDTO updateAppointment(String codeService, UpdateAppointmentReqDTO req) {
        Appointment ap = appointmentRepository.findAppointmentByCode(codeService)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment " + codeService + " not found"));

        // --- LÓGICA DE HORARIOS: Si cambia la fecha/hora, liberar el viejo y tomar el nuevo ---
        if (!ap.getAppointmentDate().equals(req.appointmentDate())) {
            freeSlot(ap.getAppointmentDate());
            bookSlot(req.appointmentDate());
        }

        ap.setReasonForVisit(req.reasonForVisit());
        ap.setAppointmentDate(req.appointmentDate());
        ap.setUpdateAt(req.updateAt());
        ap.setServiceType(ServiceType.valueOf(req.serviceType()));
        ap.setStatus(Status.valueOf(req.status()));

        appointmentRepository.save(ap);

        Pet pet = petRepository.findById(ap.getPetIdRef())
                .orElseThrow(() -> new PetNotFoundException("Pet " + ap.getPetIdRef() + " not found"));

        return buildResDTO(ap, pet);
    }

    @Transactional
    public void deleteAppointment(String codeService) {
        Appointment ap = appointmentRepository.findAppointmentByCode(codeService)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment " + codeService + " not found"));

        // --- Liberar el horario antes de eliminar ---
        freeSlot(ap.getAppointmentDate());

        appointmentRepository.delete(ap);
    }

    @Transactional
    public AppointmentResDTO updateAppointmentStatus(String codeService, String newStatus) {
        Appointment ap = appointmentRepository.findAppointmentByCode(codeService)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment " + codeService + " not found"));

        Status status = Status.valueOf(newStatus.toUpperCase());
        ap.setStatus(status);

        // --- LÓGICA DE HORARIOS: Liberar si se cancela ---
        if (status == Status.CANCELLED) {
            freeSlot(ap.getAppointmentDate());
        }

        appointmentRepository.save(ap);

        Pet pet = petRepository.findById(ap.getPetIdRef())
                .orElseThrow(() -> new PetNotFoundException("Pet " + ap.getPetIdRef() + " not found"));

        return buildResDTO(ap, pet);
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    private void bookSlot(LocalDateTime appointmentDate) {
        AvailableSlot slotToBook = availableSlotRepository.findBySlotDateAndStartTime(
                appointmentDate.toLocalDate(),
                appointmentDate.toLocalTime()
        ).orElseThrow(() -> new RuntimeException("El horario solicitado no existe."));

        if (!slotToBook.isAvailable()) {
            throw new RuntimeException("El horario seleccionado ya no está disponible.");
        }

        slotToBook.setAvailable(false);
        availableSlotRepository.save(slotToBook);
    }

    private void freeSlot(LocalDateTime appointmentDate) {
        availableSlotRepository.findBySlotDateAndStartTime(
                appointmentDate.toLocalDate(),
                appointmentDate.toLocalTime()
        ).ifPresent(slot -> {
            slot.setAvailable(true);
            availableSlotRepository.save(slot);
        });
    }

    private AppointmentResDTO buildResDTO(Appointment ap, Pet pet) {
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
    }
}