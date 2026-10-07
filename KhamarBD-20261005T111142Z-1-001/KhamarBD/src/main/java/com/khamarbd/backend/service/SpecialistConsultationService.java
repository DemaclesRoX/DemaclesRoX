package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.SpecialistConsultationRequestDto;
import com.khamarbd.backend.entity.LotOrAnimal;
import com.khamarbd.backend.entity.SpecialistConsultation;
import com.khamarbd.backend.entity.User;
import com.khamarbd.backend.repository.LotOrAnimalRepository;
import com.khamarbd.backend.repository.SpecialistConsultationRepository;
import com.khamarbd.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class SpecialistConsultationService {

    @Autowired
    private SpecialistConsultationRepository specialistConsultationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LotOrAnimalRepository lotOrAnimalRepository;

    public SpecialistConsultation saveConsultation(SpecialistConsultationRequestDto dto) {
        SpecialistConsultation consultation = new SpecialistConsultation();
        User farmer = userRepository.findById(dto.getFarmerId()).orElse(null);
        User specialist = userRepository.findById(dto.getSpecialistId()).orElse(null);

        consultation.setFarmer(farmer);
        consultation.setSpecialist(specialist);
        if (dto.getLotId() != null) {
            LotOrAnimal lot = lotOrAnimalRepository.findById(dto.getLotId()).orElse(null);
            consultation.setLotOrAnimal(lot);
        }
        consultation.setAppointmentDate(parseAppointmentDate(dto.getAppointmentDate()));
        return specialistConsultationRepository.save(consultation);
    }

    public SpecialistConsultation updateConsultation(SpecialistConsultation consultation) {
        return specialistConsultationRepository.save(consultation);
    }

    // FIX (blocker): appointment_date is nullable = false, but the old code called
    // ZonedDateTime.parse(...) on the raw request value. A plain date sent by a client
    // ("2026-10-05") is NOT a valid ZonedDateTime -> DateTimeParseException -> 500 on
    // POST /consultation/create. This helper accepts BOTH forms:
    //   "2026-10-05"                       -> start of that day in the JVM zone
    //   "2026-10-05T10:00:00+06:00"        -> parsed as a full zoned timestamp
    private ZonedDateTime parseAppointmentDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return ZonedDateTime.now();
        }
        String value = raw.trim();
        try {
            return LocalDate.parse(value).atStartOfDay(ZoneId.systemDefault());
        } catch (DateTimeParseException notADateOnlyValue) {
            return ZonedDateTime.parse(value);
        }
    }

    public List<SpecialistConsultation> getAllConsultations() {
        return specialistConsultationRepository.findAll();
    }

    public SpecialistConsultation getConsultationById(Long id) {
        return specialistConsultationRepository.findById(id).orElse(null);
    }

    public List<SpecialistConsultation> getConsultationsByFarmerId(Long farmerId) {
        return specialistConsultationRepository.findByFarmer_UserId(farmerId);
    }

    public List<SpecialistConsultation> getConsultationsBySpecialistId(Long specialistId) {
        return specialistConsultationRepository.findBySpecialist_UserId(specialistId);
    }

    public List<SpecialistConsultation> getConsultationsByUserId(Long userId) {
        return specialistConsultationRepository.findByParticipantUserId(userId);
    }

    public SpecialistConsultation getOrCreateThread(Long user1Id, Long user2Id, String topic, Long lotId) {
        List<SpecialistConsultation> existing = specialistConsultationRepository.findBetweenUsers(user1Id, user2Id);
        if (existing != null && !existing.isEmpty()) {
            return existing.get(0);
        }
        User u1 = userRepository.findById(user1Id).orElse(null);
        User u2 = userRepository.findById(user2Id).orElse(null);
        if (u1 == null || u2 == null) {
            throw new IllegalArgumentException("User not found (user1: " + user1Id + ", user2: " + user2Id + ")");
        }

        SpecialistConsultation thread = new SpecialistConsultation();
        thread.setFarmer(u1);
        thread.setSpecialist(u2);
        if (lotId != null) {
            LotOrAnimal lot = lotOrAnimalRepository.findById(lotId).orElse(null);
            thread.setLotOrAnimal(lot);
        }
        thread.setAppointmentDate(ZonedDateTime.now());
        thread.setStatus("ACCEPTED");
        if (topic != null && !topic.isBlank()) {
            thread.setProblemDescription(topic);
        }
        thread.setCreatedAt(ZonedDateTime.now());
        thread.setUpdatedAt(ZonedDateTime.now());
        return specialistConsultationRepository.save(thread);
    }
}
