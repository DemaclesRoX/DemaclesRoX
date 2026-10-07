package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.PrescriptionItemRequestDto;
import com.khamarbd.backend.dto.PrescriptionRequestDto;
import com.khamarbd.backend.entity.LotOrAnimal;
import com.khamarbd.backend.entity.Prescription;
import com.khamarbd.backend.entity.PrescriptionItem;
import com.khamarbd.backend.entity.SpecialistConsultation;
import com.khamarbd.backend.entity.User;
import com.khamarbd.backend.repository.LotOrAnimalRepository;
import com.khamarbd.backend.repository.PrescriptionRepository;
import com.khamarbd.backend.repository.SpecialistConsultationRepository;
import com.khamarbd.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private SpecialistConsultationRepository specialistConsultationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LotOrAnimalRepository lotOrAnimalRepository;

    public Prescription savePrescription(PrescriptionRequestDto dto) {
        Prescription rx = new Prescription();
        SpecialistConsultation consultation = null;

        if (dto.getConsultationId() != null) {
            consultation = specialistConsultationRepository.findById(dto.getConsultationId()).orElse(null);
            rx.setConsultation(consultation);
        }

        User specialist = null;
        if (dto.getSpecialistId() != null) {
            specialist = userRepository.findById(dto.getSpecialistId()).orElse(null);
        }
        if (specialist == null && consultation != null) {
            specialist = consultation.getSpecialist();
        }
        rx.setSpecialist(specialist);

        User farmer = null;
        if (dto.getFarmerId() != null) {
            farmer = userRepository.findById(dto.getFarmerId()).orElse(null);
        }
        if (farmer == null && consultation != null) {
            farmer = consultation.getFarmer();
        }
        rx.setFarmer(farmer);

        LotOrAnimal lot = null;
        if (dto.getLotId() != null) {
            lot = lotOrAnimalRepository.findById(dto.getLotId()).orElse(null);
        }
        if (lot == null && consultation != null) {
            lot = consultation.getLotOrAnimal();
        }
        rx.setLotOrAnimal(lot);

        String diagnosis = dto.getDiagnosis();
        if (diagnosis == null || diagnosis.isBlank()) {
            diagnosis = dto.getNotes() != null && !dto.getNotes().isBlank() ? dto.getNotes() : "Clinical Advisory Prescription";
        }
        rx.setDiagnosis(diagnosis);
        rx.setSymptomsNote(dto.getSymptomsNote() != null ? dto.getSymptomsNote() : dto.getNotes());
        rx.setSpecialNotice(dto.getSpecialNotice());
        rx.setTemplateCode(dto.getTemplateCode());
        rx.setDigitalSignature(dto.getDigitalSignature() != null ? dto.getDigitalSignature() : (specialist != null ? specialist.getName() : "Certified Specialist"));
        rx.setIssuedAt(ZonedDateTime.now());
        rx.setStatus("ISSUED");

        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            List<PrescriptionItem> items = new ArrayList<>();
            int order = 1;
            for (PrescriptionItemRequestDto itemDto : dto.getItems()) {
                if (itemDto.getMedicineName() == null || itemDto.getMedicineName().isBlank()) continue;
                PrescriptionItem item = new PrescriptionItem();
                item.setPrescription(rx);
                item.setItemName(itemDto.getMedicineName());
                item.setCategoryCode(itemDto.getCategoryCode() != null ? itemDto.getCategoryCode() : "MEDICINE");
                item.setDosage(itemDto.getDosage() != null ? itemDto.getDosage() : "As advised");
                item.setInstructions(itemDto.getInstructions() != null ? itemDto.getInstructions() : itemDto.getDuration());
                item.setDurationDays(itemDto.getDurationDays() != null ? itemDto.getDurationDays() : 5);
                item.setSortOrder(itemDto.getSortOrder() != null ? itemDto.getSortOrder() : order++);
                items.add(item);
            }
            rx.setItems(items);
        }

        Prescription saved = prescriptionRepository.save(rx);

        // If linked to a consultation, mark consultation as PRESCRIBED
        if (consultation != null) {
            consultation.setStatus("PRESCRIBED");
            consultation.setPrescriptionNotes(rx.getDiagnosis());
            specialistConsultationRepository.save(consultation);
        }

        return saved;
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id).orElse(null);
    }

    public List<Prescription> getPrescriptionsByFarmerId(Long farmerId) {
        return prescriptionRepository.findByFarmer_UserId(farmerId);
    }

    public List<Prescription> getPrescriptionsBySpecialistId(Long specialistId) {
        return prescriptionRepository.findBySpecialist_UserId(specialistId);
    }
}
