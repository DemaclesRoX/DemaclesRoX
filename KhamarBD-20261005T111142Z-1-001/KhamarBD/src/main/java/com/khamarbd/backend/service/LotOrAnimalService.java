package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.LotOrAnimalRequestDto;
import com.khamarbd.backend.entity.Farm;
import com.khamarbd.backend.entity.LotOrAnimal;
import com.khamarbd.backend.repository.FarmRepository;
import com.khamarbd.backend.repository.LotOrAnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LotOrAnimalService {

    @Autowired
    private LotOrAnimalRepository lotOrAnimalRepository;

    @Autowired
    private FarmRepository farmRepository;

    public LotOrAnimal saveLotOrAnimal(LotOrAnimalRequestDto dto) {
        LotOrAnimal lot = new LotOrAnimal();
        Farm farm = farmRepository.findById(dto.getFarmId()).orElse(null);
        lot.setFarm(farm);
        lot.setIdentifierTag(dto.getIdentifierTag());
        lot.setCategory(dto.getCategory());
        lot.setSpeciesOrBreed(dto.getSpeciesOrBreed());

        // FIX (blocker): LotOrAnimal.unitKind is nullable = false, but nothing ever set it
        // -> DataIntegrityViolationException -> 500 on every POST /lot/create.
        // The request DTO only carries "category" (Individual | Lot), so unitKind is
        // derived from it: Individual -> ANIMAL, everything else -> BATCH.
        lot.setUnitKind("Individual".equalsIgnoreCase(dto.getCategory()) ? "ANIMAL" : "BATCH");

        if (dto.getStartDate() != null && !dto.getStartDate().isBlank()) {
            lot.setStartDate(LocalDate.parse(dto.getStartDate()));
        }
        return lotOrAnimalRepository.save(lot);
    }

    public List<LotOrAnimal> getAllUnits() {
        return lotOrAnimalRepository.findAll();
    }

    public LotOrAnimal getUnitById(Long id) {
        return lotOrAnimalRepository.findById(id).orElse(null);
    }

    public List<LotOrAnimal> getUnitsByFarmId(Long farmId) {
        return lotOrAnimalRepository.findByFarm_FarmId(farmId);
    }

    public List<LotOrAnimal> getUnitsByUserId(Long userId) {
        return lotOrAnimalRepository.findByFarm_User_UserId(userId);
    }
}
