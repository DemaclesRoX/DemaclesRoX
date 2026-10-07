package com.khamarbd.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "order_transaction")
public class OrderTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Column(name = "order_reference", length = 40)
    private String orderReference;

    @Column(name = "seller_id")
    private Long sellerId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "listing_id")
    private MarketplaceListing listing;

    @Column(name = "quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "total_agreed_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAgreedPrice;

    @Column(name = "order_status")
    private String orderStatus = "PENDING";

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "payment_note", length = 160)
    private String paymentNote;

    @Column(name = "buyer_rating")
    private Integer buyerRating;

    @Column(name = "seller_rating")
    private Integer sellerRating;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public User getBuyer() { return buyer; }
    public void setBuyer(User buyer) { this.buyer = buyer; }
    public MarketplaceListing getListing() { return listing; }
    public void setListing(MarketplaceListing listing) { this.listing = listing; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getTotalAgreedPrice() { return totalAgreedPrice; }
    public void setTotalAgreedPrice(BigDecimal totalAgreedPrice) { this.totalAgreedPrice = totalAgreedPrice; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public String getPaymentNote() { return paymentNote; }
    public void setPaymentNote(String paymentNote) { this.paymentNote = paymentNote; }
    public Integer getBuyerRating() { return buyerRating; }
    public void setBuyerRating(Integer buyerRating) { this.buyerRating = buyerRating; }
    public Integer getSellerRating() { return sellerRating; }
    public void setSellerRating(Integer sellerRating) { this.sellerRating = sellerRating; }
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    public String getOrderReference() { return orderReference; }
    public void setOrderReference(String orderReference) { this.orderReference = orderReference; }
    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper serialization getters for Frontend
    public Long getBuyerId() { return buyer != null ? buyer.getUserId() : null; }
    public String getBuyerName() { return buyer != null ? buyer.getName() : "Customer"; }
    public String getBuyerPhone() { return buyer != null ? buyer.getPhone() : null; }
    public String getBuyerDistrict() { return buyer != null ? buyer.getDistrict() : null; }
    public String getBuyerUpazila() { return buyer != null ? buyer.getUpazila() : null; }

    public Long getListingId() { return listing != null ? listing.getListingId() : null; }
    public String getListingTitle() { return listing != null ? listing.getTitle() : "Product Listing"; }
    public String getListingUnit() { return listing != null ? listing.getUnit() : "Unit"; }
    public String getListingCategory() { return listing != null ? listing.getProductCategory() : null; }
    public String getListingImage() { return listing != null ? listing.getImageUrls() : null; }
    public BigDecimal getListingUnitPrice() { return listing != null ? listing.getPrice() : null; }
    public String getSellerName() { return listing != null && listing.getSeller() != null ? listing.getSeller().getName() : "Supplier"; }
    public String getSellerPhone() { return listing != null && listing.getSeller() != null ? listing.getSeller().getPhone() : null; }
}
