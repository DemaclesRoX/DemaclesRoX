package com.khamarbd.backend.dto;

import jakarta.validation.constraints.*;

public class OrderTransactionRequestDto {

    @NotNull
    private Long buyerId;

    @NotNull
    private Long listingId;

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;

    @Positive(message = "totalAgreedPrice must be greater than 0")
    private double totalAgreedPrice;

    private Long sellerId;

    private String paymentNote;

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAgreedPrice() {
        return totalAgreedPrice;
    }

    public void setTotalAgreedPrice(double totalAgreedPrice) {
        this.totalAgreedPrice = totalAgreedPrice;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getPaymentNote() {
        return paymentNote;
    }

    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
    }
}
