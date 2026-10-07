package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.FarmLedgerRequestDto;
import com.khamarbd.backend.entity.FarmLedger;
import com.khamarbd.backend.service.FarmLedgerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ledger")
public class FarmLedgerController {

    @Autowired
    private FarmLedgerService farmLedgerService;

    // @RequestBody — log a feed/medicine/sale entry against a lot
    @PostMapping("/create")
    public ResponseEntity<?> createLedgerEntry(
            @Valid @RequestBody FarmLedgerRequestDto ledgerRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        FarmLedger savedEntry = farmLedgerService.saveLedgerEntry(ledgerRequestDto);
        return ResponseEntity.ok(savedEntry);
    }

    // @RequestParam — search ledger entries by lotId or farmId
    @GetMapping("/search")
    public ResponseEntity<List<FarmLedger>> searchLedger(
            @RequestParam(required = false) Long lotId,
            @RequestParam(required = false) Long farmId
    ) {
        List<FarmLedger> entries;
        if (lotId != null) {
            entries = farmLedgerService.getEntriesByLotId(lotId);
        } else if (farmId != null) {
            entries = farmLedgerService.getEntriesByFarmId(farmId);
        } else {
            entries = farmLedgerService.getAllEntries();
        }
        return ResponseEntity.ok(entries);
    }

    // @PathVariable — fetch one ledger entry's details by id
    @GetMapping("/filter/{ledgerId}/details")
    public ResponseEntity<?> getLedgerDetails(@PathVariable Long ledgerId) {
        FarmLedger entry = farmLedgerService.getEntryById(ledgerId);
        if (entry == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(entry);
    }
}
