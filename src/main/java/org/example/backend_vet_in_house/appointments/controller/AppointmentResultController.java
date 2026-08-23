package org.example.backend_vet_in_house.appointments.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.appointments.dto.req.CreateAppointmentResultReqDTO;
import org.example.backend_vet_in_house.appointments.dto.req.UpdateAppointmentResultReqDTO;
import org.example.backend_vet_in_house.appointments.service.AppointmentResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/appointment-result")
@RequiredArgsConstructor
public class AppointmentResultController {

    private final AppointmentResultService appointmentResultService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointmentResult(
            @RequestParam String codeService,
            @RequestBody CreateAppointmentResultReqDTO req) {
        return new ResponseEntity<>(appointmentResultService.createAppointmentResult(codeService, req), HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getResultByAppointmentCode(@RequestParam String codeService) {
        return new ResponseEntity<>(appointmentResultService.getResultByAppointmentCode(codeService), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAppointmentResult(
            @RequestParam String codeService,
            @RequestBody UpdateAppointmentResultReqDTO req) {
        return new ResponseEntity<>(appointmentResultService.updateAppointmentResult(codeService, req), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAppointmentResult(@RequestParam String codeService) {
        appointmentResultService.deleteAppointmentResult(codeService);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}