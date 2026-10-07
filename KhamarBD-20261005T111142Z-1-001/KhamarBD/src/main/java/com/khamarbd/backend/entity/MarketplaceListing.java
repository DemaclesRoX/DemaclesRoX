package com.khamarbd.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "marketplace_listing")
public class MarketplaceListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listing_id")
    private Long listingId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "lot_id")
    private LotOrAnimal lotOrAnimal;

    @Column(name = "title", nullable = false, length = 160)
    private String title;

    @Column(name = "product_category", nullable = false, length = 60)
    private String productCategory;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity_available", nullable = false, precision = 12, scale = 2)
    private BigDecimal quantityAvailable;

    @Column(name = "unit", nullable = false, length = 20)
    private String unit;

    @Column(name = "listing_status")
    private String listingStatus = "ACTIVE";

    @Column(name = "listing_tab", nullable = false)
    private String listingTab;

    @Column(name = "farm_sector")
    private String farmSector;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "taxonomy_code", length = 40)
    private String taxonomyCode;

    @Column(name = "division", nullable = false, length = 60)
    private String division;

    @Column(name = "district", nullable = false, length = 60)
    private String district;

    @Column(name = "upazila", nullable = false, length = 60)
    private String upazila;

    @Column(name = "image_urls", columnDefinition = "JSON")
    private String imageUrls;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @OneToMany(mappedBy = "listing")
    private List<OrderTransaction> orders;

    @JsonIgnore
    @ManyToMany(mappedBy = "savedListings")
    private List<User> savedByUsers;

    // Getters and Setters
    public Long getListingId() { return listingId; }
    public void setListingId(Long listingId) { this.listingId = listingId; }
    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }
    public LotOrAnimal getLotOrAnimal() { return lotOrAnimal; }
    public void setLotOrAnimal(LotOrAnimal lotOrAnimal) { this.lotOrAnimal = lotOrAnimal; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(BigDecimal quantityAvailable) { this.quantityAvailable = quantityAvailable; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getListingStatus() { return listingStatus; }
    public void setListingStatus(String listingStatus) { this.listingStatus = listingStatus; }
    public String getListingTab() { return listingTab; }
    public void setListingTab(String listingTab) { this.listingTab = listingTab; }
    public String getFarmSector() { return farmSector; }
    public void setFarmSector(String farmSector) { this.farmSector = farmSector; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTaxonomyCode() { return taxonomyCode; }
    public void setTaxonomyCode(String taxonomyCode) { this.taxonomyCode = taxonomyCode; }
    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getUpazila() { return upazila; }
    public void setUpazila(String upazila) { this.upazila = upazila; }
    public String getImageUrls() { return imageUrls; }
    public void setImageUrls(String imageUrls) { this.imageUrls = imageUrls; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<OrderTransaction> getOrders() { return orders; }
    public void setOrders(List<OrderTransaction> orders) { this.orders = orders; }
    public List<User> getSavedByUsers() { return savedByUsers; }
    public void setSavedByUsers(List<User> savedByUsers) { this.savedByUsers = savedByUsers; }

    // Helper getters for JSON serialization
    public Long getSellerId() {
        return seller != null ? seller.getUserId() : null;
    }

    public String getSellerName() {
        return seller != null ? seller.getName() : null;
    }

    public String getSellerPhone() {
        return seller != null ? seller.getPhone() : null;
    }

    public String getSellerRole() {
        return seller != null ? seller.getPrimaryRole() : null;
    }

    public Long getLotId() {
        return lotOrAnimal != null ? lotOrAnimal.getLotId() : null;
    }

    public String getLotTag() {
        return lotOrAnimal != null ? lotOrAnimal.getIdentifierTag() : null;
    }
}
