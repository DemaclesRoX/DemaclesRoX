package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.FarmLedgerRequestDto;
import com.khamarbd.backend.entity.FarmLedger;
import com.khamarbd.backend.entity.LotOrAnimal;
import com.khamarbd.backend.entity.Farm;
import com.khamarbd.backend.repository.FarmLedgerRepository;
import com.khamarbd.backend.repository.FarmRepository;
import com.khamarbd.backend.repository.LotOrAnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FarmLedgerService {

    @Autowired
    private FarmLedgerRepository farmLedgerRepository;

    @Autowired
    private LotOrAnimalRepository lotOrAnimalRepository;

    @Autowired
    private FarmRepository farmRepository;

    public FarmLedger saveLedgerEntry(FarmLedgerRequestDto dto) {
        FarmLedger ledger = new FarmLedger();
        
        if (dto.getLotId() != null) {
            LotOrAnimal lot = lotOrAnimalRepository.findById(dto.getLotId()).orElse(null);
            ledger.setLotOrAnimal(lot);
            if (lot != null && lot.getFarm() != null) {
                ledger.setFarm(lot.getFarm());
            }
        } else if (dto.getFarmId() != null) {
            Farm farm = farmRepository.findById(dto.getFarmId()).orElse(null);
            ledger.setFarm(farm);
        }

        ledger.setEntryType(dto.getEntryType() != null ? dto.getEntryType().trim().toUpperCase() : "EXPENSE");
        ledger.setCategory(dto.getCategory() != null ? dto.getCategory().trim() : "General");
        ledger.setAmount(BigDecimal.valueOf(dto.getAmount()));
        
        if (dto.getEventDate() != null && !dto.getEventDate().isBlank()) {
            String rawDate = dto.getEventDate().trim();
            if (rawDate.contains("T")) {
                rawDate = rawDate.substring(0, rawDate.indexOf("T"));
            }
            try {
                ledger.setEventDate(LocalDate.parse(rawDate));
            } catch (Exception ex) {
                ledger.setEventDate(LocalDate.now());
            }
        } else {
            ledger.setEventDate(LocalDate.now());
        }

        ledger.setNotes(dto.getNotes());
        if (dto.getCreatedBy() != null) {
            ledger.setCreatedBy(dto.getCreatedBy());
        }
        return farmLedgerRepository.save(ledger);
    }

    public List<FarmLedger> getAllEntries() {
        return farmLedgerRepository.findAll();
    }

    public FarmLedger getEntryById(Long id) {
        return farmLedgerRepository.findById(id).orElse(null);
    }

    public List<FarmLedger> getEntriesByLotId(Long lotId) {
        return farmLedgerRepository.findByLotOrAnimal_LotIdOrderByEventDateDesc(lotId);
    }

    public List<FarmLedger> getEntriesByFarmId(Long farmId) {
        return farmLedgerRepository.findByFarm_FarmIdOrderByEventDateDesc(farmId);
    }
}
