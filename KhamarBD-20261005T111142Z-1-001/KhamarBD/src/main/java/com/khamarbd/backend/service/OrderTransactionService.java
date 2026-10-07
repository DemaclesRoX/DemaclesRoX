package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.OrderTransactionRequestDto;
import com.khamarbd.backend.entity.Farm;
import com.khamarbd.backend.entity.FarmLedger;
import com.khamarbd.backend.entity.MarketplaceListing;
import com.khamarbd.backend.entity.OrderTransaction;
import com.khamarbd.backend.entity.User;
import com.khamarbd.backend.repository.FarmLedgerRepository;
import com.khamarbd.backend.repository.FarmRepository;
import com.khamarbd.backend.repository.MarketplaceListingRepository;
import com.khamarbd.backend.repository.OrderTransactionRepository;
import com.khamarbd.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderTransactionService {

    @Autowired
    private OrderTransactionRepository orderTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarketplaceListingRepository marketplaceListingRepository;

    @Autowired
    private FarmLedgerRepository farmLedgerRepository;

    @Autowired
    private FarmRepository farmRepository;

    public OrderTransaction saveOrder(OrderTransactionRequestDto dto) {
        OrderTransaction order = new OrderTransaction();
        User buyer = userRepository.findById(dto.getBuyerId()).orElse(null);
        MarketplaceListing listing = marketplaceListingRepository.findById(dto.getListingId()).orElse(null);

        order.setBuyer(buyer);
        order.setListing(listing);
        if (dto.getSellerId() != null) {
            order.setSellerId(dto.getSellerId());
        } else if (listing != null && listing.getSeller() != null) {
            order.setSellerId(listing.getSeller().getUserId());
        }
        if (dto.getPaymentNote() != null && !dto.getPaymentNote().trim().isEmpty()) {
            order.setPaymentNote(dto.getPaymentNote().trim());
        }
        order.setQuantity(BigDecimal.valueOf(dto.getQuantity()));
        order.setTotalAgreedPrice(BigDecimal.valueOf(dto.getTotalAgreedPrice()));
        order.setOrderReference("ORD-" + System.currentTimeMillis());
        OrderTransaction savedOrder = orderTransactionRepository.save(order);

        // --- AUTOMATIC LEDGER INTEGRATION ---
        // 1. Seller Ledger Entry (INCOME)
        if (listing != null && listing.getLotOrAnimal() != null && listing.getLotOrAnimal().getFarm() != null) {
            FarmLedger income = new FarmLedger();
            income.setFarm(listing.getLotOrAnimal().getFarm());
            income.setLotOrAnimal(listing.getLotOrAnimal());
            income.setEntryType("INCOME");
            income.setCategory("Sales Revenue");
            income.setAmount(savedOrder.getTotalAgreedPrice());
            income.setEventDate(LocalDate.now());
            income.setNotes("Sold via Marketplace: " + listing.getTitle() + " (Order: " + savedOrder.getOrderReference() + ")");
            if (listing.getSeller() != null) {
                income.setCreatedBy(listing.getSeller().getUserId());
            }
            farmLedgerRepository.save(income);
        }

        // 2. Buyer Ledger Entry (EXPENSE)
        if (buyer != null) {
            // Defaulting to the buyer's first farm if they have one
            List<Farm> buyerFarms = farmRepository.findByUser_UserId(buyer.getUserId());
            if (buyerFarms != null && !buyerFarms.isEmpty()) {
                Farm buyerFarm = buyerFarms.get(0);
                FarmLedger expense = new FarmLedger();
                expense.setFarm(buyerFarm);
                expense.setEntryType("EXPENSE");
                expense.setCategory("Marketplace Purchase");
                expense.setAmount(savedOrder.getTotalAgreedPrice());
                expense.setEventDate(LocalDate.now());
                expense.setNotes("Purchased from Marketplace: " + (listing != null ? listing.getTitle() : "Items") + " (Order: " + savedOrder.getOrderReference() + ")");
                expense.setCreatedBy(buyer.getUserId());
                farmLedgerRepository.save(expense);
            }
        }

        return savedOrder;
    }

    public List<OrderTransaction> getAllOrders() {
        return orderTransactionRepository.findAll();
    }

    public OrderTransaction getOrderById(Long id) {
        return orderTransactionRepository.findById(id).orElse(null);
    }

    public List<OrderTransaction> getOrdersByBuyerId(Long buyerId) {
        return orderTransactionRepository.findByBuyer_UserId(buyerId);
    }

    public List<OrderTransaction> getOrdersBySellerId(Long sellerId) {
        return orderTransactionRepository.findBySellerId(sellerId);
    }

    public OrderTransaction updateOrderStatus(Long orderId, String status, String note) {
        OrderTransaction order = orderTransactionRepository.findById(orderId).orElse(null);
        if (order != null) {
            if (status != null && !status.trim().isEmpty()) {
                order.setOrderStatus(status.trim().toUpperCase());
            }
            if (note != null && !note.trim().isEmpty()) {
                order.setPaymentNote(note.trim());
            }
            order.setUpdatedAt(java.time.ZonedDateTime.now());
            return orderTransactionRepository.save(order);
        }
        return null;
    }

    public boolean deleteOrder(Long orderId) {
        if (orderTransactionRepository.existsById(orderId)) {
            orderTransactionRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
}
