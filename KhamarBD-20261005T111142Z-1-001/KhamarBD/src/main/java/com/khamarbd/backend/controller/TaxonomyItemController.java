package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.TaxonomyItemRequestDto;
import com.khamarbd.backend.entity.support.TaxonomyItem;
import com.khamarbd.backend.service.support.TaxonomyItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taxonomy")
public class TaxonomyItemController {

    @Autowired
    private TaxonomyItemService taxonomyItemService;

    // @RequestBody — admin seeds a new taxonomy dropdown entry
    @PostMapping("/create")
    public ResponseEntity<?> createTaxonomyItem(
            @Valid @RequestBody TaxonomyItemRequestDto taxonomyItemRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        TaxonomyItem saved = taxonomyItemService.saveTaxonomyItem(taxonomyItemRequestDto);
        return ResponseEntity.ok(saved);
    }

    // @RequestParam — fetch dropdown items by domain (e.g. INPUT, UNIT, LEDGER_CATEGORY)
    @GetMapping("/search")
    public ResponseEntity<List<TaxonomyItem>> searchTaxonomy(
            @RequestParam String domain,
            @RequestParam(required = false) String sector
    ) {
        List<TaxonomyItem> items = (sector != null && !sector.isBlank())
                ? taxonomyItemService.getItemsByDomainAndSector(domain, sector)
                : taxonomyItemService.getItemsByDomain(domain);
        return ResponseEntity.ok(items);
    }
}
