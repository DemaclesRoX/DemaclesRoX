package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.FarmRequestDto;
import com.khamarbd.backend.entity.Farm;
import com.khamarbd.backend.service.FarmService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/farm")
public class FarmController {

    @Autowired
    private FarmService farmService;

    // @RequestBody — a farmer creates a new farm unit under their account
    @PostMapping("/create")
    public ResponseEntity<?> createFarm(
            @Valid @RequestBody FarmRequestDto farmRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        Farm savedFarm = farmService.saveFarm(farmRequestDto);
        return ResponseEntity.ok(savedFarm);
    }

    // @RequestParam — search farms by farmType (required) and userId (optional)
    @GetMapping("/search")
    public ResponseEntity<List<Farm>> searchFarms(
            @RequestParam(required = false) String farmType,
            @RequestParam(required = false) Long userId
    ) {
        List<Farm> farms;
        if (userId != null) {
            farms = farmService.getFarmsByUserId(userId);
        } else if (farmType != null && !farmType.isBlank()) {
            farms = farmService.getFarmsBySector(farmType);
        } else {
            farms = farmService.getAllFarms();
        }
        return ResponseEntity.ok(farms);
    }

    // @PathVariable — fetch one farm's details by id
    @GetMapping("/filter/{farmId}/details")
    public ResponseEntity<?> getFarmDetails(@PathVariable Long farmId) {
        Farm farm = farmService.getFarmById(farmId);
        if (farm == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(farm);
    }
}
