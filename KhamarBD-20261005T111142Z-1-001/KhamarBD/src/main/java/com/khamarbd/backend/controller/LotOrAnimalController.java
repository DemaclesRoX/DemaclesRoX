package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.LotOrAnimalRequestDto;
import com.khamarbd.backend.entity.LotOrAnimal;
import com.khamarbd.backend.service.LotOrAnimalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lot")
public class LotOrAnimalController {

    @Autowired
    private LotOrAnimalService lotOrAnimalService;

    // @RequestBody — a farmer creates a tracking unit (batch/pond/plot/animal)
    @PostMapping("/create")
    public ResponseEntity<?> createLot(
            @Valid @RequestBody LotOrAnimalRequestDto lotRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        LotOrAnimal savedLot = lotOrAnimalService.saveLotOrAnimal(lotRequestDto);
        return ResponseEntity.ok(savedLot);
    }

    // @RequestParam — search units by farmId or userId
    @GetMapping("/search")
    public ResponseEntity<List<LotOrAnimal>> searchLots(
            @RequestParam(required = false) Long farmId,
            @RequestParam(required = false) Long userId
    ) {
        List<LotOrAnimal> lots;
        if (farmId != null) {
            lots = lotOrAnimalService.getUnitsByFarmId(farmId);
        } else if (userId != null) {
            lots = lotOrAnimalService.getUnitsByUserId(userId);
        } else {
            lots = lotOrAnimalService.getAllUnits();
        }
        return ResponseEntity.ok(lots);
    }

    // @PathVariable — fetch one lot/animal's details by id
    @GetMapping("/filter/{lotId}/details")
    public ResponseEntity<?> getLotDetails(@PathVariable Long lotId) {
        LotOrAnimal lot = lotOrAnimalService.getUnitById(lotId);
        if (lot == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(lot);
    }
}
