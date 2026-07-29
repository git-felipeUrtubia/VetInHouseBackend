package org.example.backend_vet_in_house.appointments.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.dto.req.CreateAppointmentReqDTO;
import org.example.backend_vet_in_house.appointments.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody CreateAppointmentReqDTO req) {
        return new ResponseEntity<>(appointmentService.createAppointment(req), HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllAppointment() {
        return new ResponseEntity<>(appointmentService.findAllAppointment(), HttpStatus.OK);
    }

}
