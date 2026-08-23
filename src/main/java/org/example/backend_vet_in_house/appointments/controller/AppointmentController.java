package org.example.backend_vet_in_house.appointments.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.dto.req.CreateAppointmentReqDTO;
import org.example.backend_vet_in_house.appointments.dto.req.UpdateAppointmentReqDTO;
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

    @GetMapping("/find")
    public ResponseEntity<?> findAppointmentByCode(@RequestParam String codeService) {
        return new ResponseEntity<>(appointmentService.findAppointmentByCode(codeService), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAppointment(
            @RequestParam String codeService,
            @RequestBody UpdateAppointmentReqDTO req) {
        return new ResponseEntity<>(appointmentService.updateAppointment(codeService, req), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAppointment(@RequestParam String codeService) {
        appointmentService.deleteAppointment(codeService);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
