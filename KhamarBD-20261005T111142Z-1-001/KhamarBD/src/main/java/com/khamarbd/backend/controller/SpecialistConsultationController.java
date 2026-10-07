package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.SpecialistConsultationRequestDto;
import com.khamarbd.backend.entity.SpecialistConsultation;
import com.khamarbd.backend.service.SpecialistConsultationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultation")
public class SpecialistConsultationController {

    @Autowired
    private SpecialistConsultationService specialistConsultationService;

    // @RequestBody — a farmer books a consultation with a specialist
    @PostMapping("/create")
    public ResponseEntity<?> createConsultation(
            @Valid @RequestBody SpecialistConsultationRequestDto consultationRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        SpecialistConsultation saved = specialistConsultationService.saveConsultation(consultationRequestDto);
        return ResponseEntity.ok(saved);
    }

    // @RequestParam — search consultations by userId (all conversations), farmerId, or specialistId
    @GetMapping("/search")
    public ResponseEntity<List<SpecialistConsultation>> searchConsultations(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long farmerId,
            @RequestParam(required = false) Long specialistId
    ) {
        List<SpecialistConsultation> consultations;
        if (userId != null) {
            consultations = specialistConsultationService.getConsultationsByUserId(userId);
        } else if (farmerId != null) {
            consultations = specialistConsultationService.getConsultationsByFarmerId(farmerId);
        } else if (specialistId != null) {
            consultations = specialistConsultationService.getConsultationsBySpecialistId(specialistId);
        } else {
            consultations = specialistConsultationService.getAllConsultations();
        }
        return ResponseEntity.ok(consultations);
    }

    // Direct Chat Thread resolution (Farmer <-> Supplier, Buyer <-> Farmer, etc.)
    public static class DirectThreadRequest {
        public Long user1Id;
        public Long user2Id;
        public String topic;
        public Long lotId;

        public Long getUser1Id() { return user1Id; }
        public void setUser1Id(Long user1Id) { this.user1Id = user1Id; }
        public Long getUser2Id() { return user2Id; }
        public void setUser2Id(Long user2Id) { this.user2Id = user2Id; }
        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public Long getLotId() { return lotId; }
        public void setLotId(Long lotId) { this.lotId = lotId; }
    }

    @PostMapping("/direct-thread")
    public ResponseEntity<?> getOrCreateDirectThread(@RequestBody DirectThreadRequest req) {
        if (req == null || req.getUser1Id() == null || req.getUser2Id() == null) {
            return ResponseEntity.badRequest().body("user1Id and user2Id are required");
        }
        try {
            SpecialistConsultation thread = specialistConsultationService.getOrCreateThread(
                    req.getUser1Id(), req.getUser2Id(), req.getTopic(), req.getLotId()
            );
            return ResponseEntity.ok(thread);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @PathVariable — fetch one consultation by id
    @GetMapping("/filter/{consultationId}/details")
    public ResponseEntity<?> getConsultationDetails(@PathVariable Long consultationId) {
        SpecialistConsultation consultation = specialistConsultationService.getConsultationById(consultationId);
        if (consultation == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(consultation);
    }

    // Accept consultation request
    @PutMapping("/{consultationId}/accept")
    public ResponseEntity<?> acceptConsultation(@PathVariable Long consultationId) {
        SpecialistConsultation consultation = specialistConsultationService.getConsultationById(consultationId);
        if (consultation == null) return ResponseEntity.notFound().build();
        consultation.setStatus("ACCEPTED");
        specialistConsultationService.updateConsultation(consultation);
        return ResponseEntity.ok(consultation);
    }
}
