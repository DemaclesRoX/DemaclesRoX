package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.SubscriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Long> {

    List<SubscriptionHistory> findByUser_UserId(Long userId);

    List<SubscriptionHistory> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    List<SubscriptionHistory> findByStatus(String status);

    Optional<SubscriptionHistory> findByTransactionId(String transactionId);
}
