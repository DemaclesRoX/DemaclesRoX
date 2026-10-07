package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.OrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {

    Optional<OrderTransaction> findByOrderReference(String orderReference);

    List<OrderTransaction> findByBuyer_UserId(Long buyerId);

    List<OrderTransaction> findBySellerId(Long sellerId);

    List<OrderTransaction> findByListing_ListingId(Long listingId);

    List<OrderTransaction> findByOrderStatus(String orderStatus);
}
