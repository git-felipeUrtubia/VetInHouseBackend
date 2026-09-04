package org.example.backend_vet_in_house.appointments.service;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.dto.req.CreateAppointmentResultReqDTO;
import org.example.backend_vet_in_house.appointments.dto.req.UpdateAppointmentResultReqDTO;
import org.example.backend_vet_in_house.appointments.dto.res.AppointmentResultResDTO;
import org.example.backend_vet_in_house.appointments.model.Appointment;
import org.example.backend_vet_in_house.appointments.model.AppointmentResult;
import org.example.backend_vet_in_house.appointments.repository.AppointmentRepository;
import org.example.backend_vet_in_house.appointments.repository.AppointmentResultRepository;
import org.example.backend_vet_in_house.shared.exception.appointment.AppointmentNotFoundException;
import org.example.backend_vet_in_house.shared.exception.appointment.AppointmentResultNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentResultService {

    private final AppointmentResultRepository appointmentResultRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public String createAppointmentResult(String codeService, CreateAppointmentResultReqDTO req) {
        Appointment appointment = appointmentRepository.findAppointmentByCode(codeService)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment " + codeService + " not found"));

        // Validar si la cita ya tiene un resultado asignado
        if (appointmentResultRepository.existsByAppointment_AppointmentId(appointment.getAppointmentId())) {
            throw new RuntimeException("Result for appointment " + codeService + " already exists");
        }

        LocalDateTime dateTime = LocalDateTime.now();

        AppointmentResult result = AppointmentResult.builder()
                .appointment(appointment)
                .diagnosis(req.diagnosis())
                .treatment(req.treatment())
                .createdAt(dateTime)
                .build();

        appointmentResultRepository.save(result);
        return "Appointment result created successfully!";
    }

    @Transactional
    public AppointmentResultResDTO getResultByAppointmentCode(String codeService) {
        Appointment appointment = appointmentRepository.findAppointmentByCode(codeService)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment " + codeService + " not found"));

        AppointmentResult result = appointmentResultRepository.findByAppointment_AppointmentId(appointment.getAppointmentId())
                .orElseThrow(() -> new AppointmentResultNotFoundException("Result not found for appointment " + codeService));

        return new AppointmentResultResDTO(
                appointment.getCodeService(),
                result.getDiagnosis(),
                result.getTreatment(),
                result.getCreatedAt()
        );
    }

    @Transactional
    public AppointmentResultResDTO updateAppointmentResult(String codeService, UpdateAppointmentResultReqDTO req) {
        Appointment appointment = appointmentRepository.findAppointmentByCode(codeService)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment " + codeService + " not found"));

        AppointmentResult result = appointmentResultRepository.findByAppointment_AppointmentId(appointment.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Result not found for appointment " + codeService));

        result.setDiagnosis(req.diagnosis());
        result.setTreatment(req.treatment());

        appointmentResultRepository.save(result);

        return new AppointmentResultResDTO(
                appointment.getCodeService(),
                result.getDiagnosis(),
                result.getTreatment(),
                result.getCreatedAt()
        );
    }

    @Transactional
    public void deleteAppointmentResult(String codeService) {
        Appointment appointment = appointmentRepository.findAppointmentByCode(codeService)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment " + codeService + " not found"));

        AppointmentResult result = appointmentResultRepository.findByAppointment_AppointmentId(appointment.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Result not found for appointment " + codeService));

        appointmentResultRepository.delete(result);
    }
}