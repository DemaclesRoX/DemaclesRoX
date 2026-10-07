package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByFarmer_UserId(Long farmerId);

    List<Prescription> findBySpecialist_UserId(Long specialistId);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Prescription p WHERE p.lotOrAnimal.lotId = :lotId")
    List<Prescription> findByLotOrAnimal_LotId(@org.springframework.data.repository.query.Param("lotId") Long lotId);

    List<Prescription> findByConsultation_ConsultationId(Long consultationId);
}
