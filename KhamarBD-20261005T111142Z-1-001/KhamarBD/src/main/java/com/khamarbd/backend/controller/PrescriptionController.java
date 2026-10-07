package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.PrescriptionRequestDto;
import com.khamarbd.backend.entity.Prescription;
import com.khamarbd.backend.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    // @RequestBody — a specialist issues a prescription linked to a consultation
    @PostMapping("/create")
    public ResponseEntity<?> createPrescription(
            @Valid @RequestBody PrescriptionRequestDto prescriptionRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        Prescription saved = prescriptionService.savePrescription(prescriptionRequestDto);
        return ResponseEntity.ok(saved);
    }

    // @RequestParam — search prescriptions by farmerId or specialistId
    @GetMapping("/search")
    public ResponseEntity<List<Prescription>> searchPrescriptions(
            @RequestParam(required = false) Long farmerId,
            @RequestParam(required = false) Long specialistId
    ) {
        List<Prescription> prescriptions;
        if (farmerId != null) {
            prescriptions = prescriptionService.getPrescriptionsByFarmerId(farmerId);
        } else if (specialistId != null) {
            prescriptions = prescriptionService.getPrescriptionsBySpecialistId(specialistId);
        } else {
            prescriptions = prescriptionService.getAllPrescriptions();
        }
        return ResponseEntity.ok(prescriptions);
    }

    // @PathVariable — fetch one prescription by id
    @GetMapping("/filter/{prescriptionId}/details")
    public ResponseEntity<?> getPrescriptionDetails(@PathVariable Long prescriptionId) {
        Prescription prescription = prescriptionService.getPrescriptionById(prescriptionId);
        if (prescription == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(prescription);
    }
}
