package com.khamarbd.backend.dto;

import jakarta.validation.constraints.*;

// Body for POST /user/register — mirrors faculty's ProductRequestDto
// validation style (@NotBlank, @Size, @Email).
public class UserRequestDto {

    @NotBlank
    @Size(min = 3, max = 50, message = "Name must be 3-50 characters")
    private String name;

    @NotBlank
    @Pattern(regexp = "^01[3-9]\\d{8}$", message = "Enter a valid BD phone number")
    private String phone;

    @NotBlank
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank
    private String primaryRole;

    @NotBlank
    private String division;

    @NotBlank
    private String district;

    @NotBlank
    private String upazila;

    private String businessName;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrimaryRole() {
        return primaryRole;
    }

    public void setPrimaryRole(String primaryRole) {
        this.primaryRole = primaryRole;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUpazila() {
        return upazila;
    }

    public void setUpazila(String upazila) {
        this.upazila = upazila;
    }
}
