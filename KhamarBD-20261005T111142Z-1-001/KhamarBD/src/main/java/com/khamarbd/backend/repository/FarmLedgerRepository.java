package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.FarmLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FarmLedgerRepository extends JpaRepository<FarmLedger, Long> {

    @org.springframework.data.jpa.repository.Query("SELECT fl FROM FarmLedger fl WHERE fl.lotOrAnimal.lotId = :lotId")
    List<FarmLedger> findByLotOrAnimal_LotId(@org.springframework.data.repository.query.Param("lotId") Long lotId);

    @org.springframework.data.jpa.repository.Query("SELECT fl FROM FarmLedger fl WHERE fl.lotOrAnimal.lotId = :lotId ORDER BY fl.eventDate DESC")
    List<FarmLedger> findByLotOrAnimal_LotIdOrderByEventDateDesc(@org.springframework.data.repository.query.Param("lotId") Long lotId);

    List<FarmLedger> findByFarm_FarmId(Long farmId);

    List<FarmLedger> findByFarm_FarmIdOrderByEventDateDesc(Long farmId);

    List<FarmLedger> findByFarm_FarmIdAndEntryType(Long farmId, String entryType);

    List<FarmLedger> findByFarm_FarmIdAndEventDateBetween(Long farmId, LocalDate startDate, LocalDate endDate);
}
