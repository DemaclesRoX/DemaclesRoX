package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.SubscriptionHistoryRequestDto;
import com.khamarbd.backend.entity.SubscriptionHistory;
import com.khamarbd.backend.service.SubscriptionHistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
public class SubscriptionHistoryController {

    @Autowired
    private SubscriptionHistoryService subscriptionHistoryService;

    // @RequestBody — a user submits subscription payment proof (bKash/Nagad)
    @PostMapping("/create")
    public ResponseEntity<?> createSubscription(
            @Valid @RequestBody SubscriptionHistoryRequestDto subscriptionRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        SubscriptionHistory saved = subscriptionHistoryService.saveSubscription(subscriptionRequestDto);
        return ResponseEntity.ok(saved);
    }

    // @RequestParam — search subscription history by userId
    @GetMapping("/search")
    public ResponseEntity<List<SubscriptionHistory>> searchSubscriptions(
            @RequestParam(required = false) Long userId
    ) {
        List<SubscriptionHistory> subs = (userId != null)
                ? subscriptionHistoryService.getSubscriptionsByUserId(userId)
                : subscriptionHistoryService.getAllSubscriptions();
        return ResponseEntity.ok(subs);
    }

    // @PathVariable — fetch one subscription record by id
    @GetMapping("/filter/{subscriptionId}/details")
    public ResponseEntity<?> getSubscriptionDetails(@PathVariable Long subscriptionId) {
        SubscriptionHistory sub = subscriptionHistoryService.getSubscriptionById(subscriptionId);
        if (sub == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sub);
    }
}
