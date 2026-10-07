package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.SubscriptionHistoryRequestDto;
import com.khamarbd.backend.entity.SubscriptionHistory;
import com.khamarbd.backend.entity.User;
import com.khamarbd.backend.repository.SubscriptionHistoryRepository;
import com.khamarbd.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionHistoryService {

    @Autowired
    private SubscriptionHistoryRepository subscriptionHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    public SubscriptionHistory saveSubscription(SubscriptionHistoryRequestDto dto) {
        SubscriptionHistory sub = new SubscriptionHistory();
        User user = userRepository.findById(dto.getUserId()).orElse(null);
        sub.setUser(user);
        sub.setPlan(dto.getPlan() != null ? dto.getPlan() : "BASIC");
        sub.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        sub.setAmount(BigDecimal.valueOf(500.00));
        sub.setPaymentMethod("BKASH");
        sub.setTransactionId("TRX-" + System.currentTimeMillis());
        if (dto.getExpiresAt() != null && !dto.getExpiresAt().isBlank()) {
            sub.setPeriodEnd(LocalDate.parse(dto.getExpiresAt()));
        }
        return subscriptionHistoryRepository.save(sub);
    }

    public List<SubscriptionHistory> getAllSubscriptions() {
        return subscriptionHistoryRepository.findAll();
    }

    public SubscriptionHistory getSubscriptionById(Long id) {
        return subscriptionHistoryRepository.findById(id).orElse(null);
    }

    public List<SubscriptionHistory> getSubscriptionsByUserId(Long userId) {
        return subscriptionHistoryRepository.findByUser_UserIdOrderByCreatedAtDesc(userId);
    }
}
