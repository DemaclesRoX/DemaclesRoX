package com.khamarbd.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "email", unique = true, length = 160)
    private String email;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "primary_role", nullable = false)
    private String primaryRole;

    @Column(name = "marketplace_mode")
    private String marketplaceMode = "BUY";

    @Column(name = "subscription_plan", length = 30)
    private String subscriptionPlan = "FREE";

    @Column(name = "subscription_status")
    private String subscriptionStatus = "NONE";

    @Column(name = "subscription_expires_at")
    private ZonedDateTime subscriptionExpiresAt;

    @Column(name = "division", length = 60)
    private String division;

    @Column(name = "district", length = 60)
    private String district;

    @Column(name = "upazila", length = 60)
    private String upazila;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "kyc_doc_type")
    private String kycDocType;

    @Column(name = "kyc_doc_ref")
    private String kycDocRef;

    @Column(name = "business_name", length = 160)
    private String businessName;

    @Column(name = "specialization_sector")
    private String specializationSector;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "registration_no")
    private String registrationNo;

    @Column(name = "preferred_language")
    private String preferredLanguage = "BN";

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Farm> farms;

    @JsonIgnore
    @OneToMany(mappedBy = "seller")
    private List<MarketplaceListing> listings;

    @JsonIgnore
    @OneToMany(mappedBy = "buyer")
    private List<OrderTransaction> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "farmer")
    private List<SpecialistConsultation> consultationsAsFarmer;

    @JsonIgnore
    @OneToMany(mappedBy = "specialist")
    private List<SpecialistConsultation> consultationsAsSpecialist;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<SubscriptionHistory> subscriptions;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_saved_listings",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "listing_id")
    )
    private List<MarketplaceListing> savedListings;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getPrimaryRole() { return primaryRole; }
    public void setPrimaryRole(String primaryRole) { this.primaryRole = primaryRole; }
    public String getRole() { return primaryRole; }
    public void setRole(String role) { this.primaryRole = role; }
    public String getMarketplaceMode() { return marketplaceMode; }
    public void setMarketplaceMode(String marketplaceMode) { this.marketplaceMode = marketplaceMode; }
    public String getSubscriptionPlan() { return subscriptionPlan; }
    public void setSubscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }
    public String getSubscriptionStatus() { return subscriptionStatus; }
    public void setSubscriptionStatus(String subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; }
    public ZonedDateTime getSubscriptionExpiresAt() { return subscriptionExpiresAt; }
    public void setSubscriptionExpiresAt(ZonedDateTime subscriptionExpiresAt) { this.subscriptionExpiresAt = subscriptionExpiresAt; }
    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getUpazila() { return upazila; }
    public void setUpazila(String upazila) { this.upazila = upazila; }
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public String getKycDocType() { return kycDocType; }
    public void setKycDocType(String kycDocType) { this.kycDocType = kycDocType; }
    public String getKycDocRef() { return kycDocRef; }
    public void setKycDocRef(String kycDocRef) { this.kycDocRef = kycDocRef; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getSpecializationSector() { return specializationSector; }
    public void setSpecializationSector(String specializationSector) { this.specializationSector = specializationSector; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getRegistrationNo() { return registrationNo; }
    public void setRegistrationNo(String registrationNo) { this.registrationNo = registrationNo; }
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    public List<Farm> getFarms() { return farms; }
    public void setFarms(List<Farm> farms) { this.farms = farms; }
    public List<MarketplaceListing> getListings() { return listings; }
    public void setListings(List<MarketplaceListing> listings) { this.listings = listings; }
    public List<OrderTransaction> getOrders() { return orders; }
    public void setOrders(List<OrderTransaction> orders) { this.orders = orders; }
    public List<SpecialistConsultation> getConsultationsAsFarmer() { return consultationsAsFarmer; }
    public void setConsultationsAsFarmer(List<SpecialistConsultation> consultationsAsFarmer) { this.consultationsAsFarmer = consultationsAsFarmer; }
    public List<SpecialistConsultation> getConsultationsAsSpecialist() { return consultationsAsSpecialist; }
    public void setConsultationsAsSpecialist(List<SpecialistConsultation> consultationsAsSpecialist) { this.consultationsAsSpecialist = consultationsAsSpecialist; }
    public List<SubscriptionHistory> getSubscriptions() { return subscriptions; }
    public void setSubscriptions(List<SubscriptionHistory> subscriptions) { this.subscriptions = subscriptions; }
    public List<MarketplaceListing> getSavedListings() { return savedListings; }
    public void setSavedListings(List<MarketplaceListing> savedListings) { this.savedListings = savedListings; }
}
