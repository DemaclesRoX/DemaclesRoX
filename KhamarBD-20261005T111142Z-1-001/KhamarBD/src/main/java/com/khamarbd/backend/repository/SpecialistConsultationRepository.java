package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.SpecialistConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistConsultationRepository extends JpaRepository<SpecialistConsultation, Long> {

    List<SpecialistConsultation> findByFarmer_UserId(Long farmerId);

    List<SpecialistConsultation> findBySpecialist_UserId(Long specialistId);

    List<SpecialistConsultation> findBySpecialist_UserIdAndStatus(Long specialistId, String status);

    @org.springframework.data.jpa.repository.Query("SELECT sc FROM SpecialistConsultation sc WHERE sc.lotOrAnimal.lotId = :lotId")
    List<SpecialistConsultation> findByLotOrAnimal_LotId(@org.springframework.data.repository.query.Param("lotId") Long lotId);

    @org.springframework.data.jpa.repository.Query("SELECT sc FROM SpecialistConsultation sc WHERE sc.farmer.userId = :userId OR sc.specialist.userId = :userId ORDER BY sc.updatedAt DESC")
    List<SpecialistConsultation> findByParticipantUserId(@org.springframework.data.repository.query.Param("userId") Long userId);

    @org.springframework.data.jpa.repository.Query("SELECT sc FROM SpecialistConsultation sc WHERE (sc.farmer.userId = :u1 AND sc.specialist.userId = :u2) OR (sc.farmer.userId = :u2 AND sc.specialist.userId = :u1) ORDER BY sc.updatedAt DESC")
    List<SpecialistConsultation> findBetweenUsers(@org.springframework.data.repository.query.Param("u1") Long u1, @org.springframework.data.repository.query.Param("u2") Long u2);
}
