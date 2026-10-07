package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {

    List<PrescriptionItem> findByPrescription_PrescriptionId(Long prescriptionId);

    List<PrescriptionItem> findByPrescription_PrescriptionIdOrderBySortOrderAsc(Long prescriptionId);
}
