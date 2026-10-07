package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.MarketplaceListingRequestDto;
import com.khamarbd.backend.entity.MarketplaceListing;
import com.khamarbd.backend.service.MarketplaceListingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listing")
public class MarketplaceListingController {

    @Autowired
    private MarketplaceListingService marketplaceListingService;

    // @RequestBody — a seller creates a new marketplace listing
    @PostMapping("/create")
    public ResponseEntity<?> createListing(
            @Valid @RequestBody MarketplaceListingRequestDto listingRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        MarketplaceListing savedListing = marketplaceListingService.saveListing(listingRequestDto);
        return ResponseEntity.ok(savedListing);
    }

    // @RequestParam — browse listings by tab (Farm Products / Farm Supplies / Specialists)
    @GetMapping("/search")
    public ResponseEntity<List<MarketplaceListing>> searchListings(
            @RequestParam(required = false) String listingTab,
            @RequestParam(required = false) Long sellerId
    ) {
        List<MarketplaceListing> listings;
        if (sellerId != null) {
            listings = marketplaceListingService.getListingsBySellerId(sellerId);
        } else if (listingTab != null && !listingTab.isBlank()) {
            listings = marketplaceListingService.getListingsByTab(listingTab);
        } else {
            listings = marketplaceListingService.getAllListings();
        }
        return ResponseEntity.ok(listings);
    }

    // @PathVariable — fetch one listing's details by id
    @GetMapping("/filter/{listingId}/details")
    public ResponseEntity<?> getListingDetails(@PathVariable Long listingId) {
        MarketplaceListing listing = marketplaceListingService.getListingById(listingId);
        if (listing == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(listing);
    }

    @RequestMapping(value = {"/filter/{listingId}/delete", "/{listingId}/delete"}, method = {RequestMethod.DELETE, RequestMethod.POST})
    public ResponseEntity<?> deleteListing(@PathVariable Long listingId) {
        boolean deleted = marketplaceListingService.deleteListing(listingId);
        if (deleted) {
            return ResponseEntity.ok(java.util.Map.of("message", "Listing deleted successfully", "listingId", listingId));
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = {"/filter/{listingId}/status", "/{listingId}/status"}, method = {RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<?> updateListingStatus(
            @PathVariable Long listingId,
            @RequestParam(required = false) String status,
            @RequestBody(required = false) java.util.Map<String, String> body
    ) {
        String finalStatus = status;
        if (body != null && body.containsKey("status")) {
            finalStatus = body.get("status");
        }
        if (finalStatus == null) finalStatus = "ACTIVE";
        MarketplaceListing updated = marketplaceListingService.updateListingStatus(listingId, finalStatus);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }
}

