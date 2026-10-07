package com.khamarbd.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "farm")
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farm_id")
    private Long farmId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "farm_name", nullable = false, length = 120)
    private String farmName;

    @Column(name = "farm_type", nullable = false)
    private String farmType;

    @Column(name = "location_details")
    private String locationDetails;

    @Column(name = "division", nullable = false, length = 60)
    private String division;

    @Column(name = "district", nullable = false, length = 60)
    private String district;

    @Column(name = "upazila", nullable = false, length = 60)
    private String upazila;

    @Column(name = "latitude", precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "farm")
    private List<LotOrAnimal> lotsOrAnimals;

    // Getters and Setters
    public Long getFarmId() { return farmId; }
    public void setFarmId(Long farmId) { this.farmId = farmId; }
    public User getUser() { return user; }
    public Long getUserId() { return user != null ? user.getUserId() : null; }
    public void setUser(User user) { this.user = user; }
    public String getFarmName() { return farmName; }
    public void setFarmName(String farmName) { this.farmName = farmName; }
    public String getName() { return farmName; }
    public void setName(String name) { this.farmName = name; }
    public String getFarmType() { return farmType; }
    public void setFarmType(String farmType) { this.farmType = farmType; }
    public String getSector() { return farmType; }
    public void setSector(String sector) { this.farmType = sector; }
    public String getLocationDetails() { return locationDetails; }
    public void setLocationDetails(String locationDetails) { this.locationDetails = locationDetails; }
    public String getVillage() { return locationDetails; }
    public void setVillage(String village) { this.locationDetails = village; }
    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getUpazila() { return upazila; }
    public void setUpazila(String upazila) { this.upazila = upazila; }
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public List<LotOrAnimal> getLotsOrAnimals() { return lotsOrAnimals; }
    public void setLotsOrAnimals(List<LotOrAnimal> lotsOrAnimals) { this.lotsOrAnimals = lotsOrAnimals; }
}
