package com.nalaka.goviya.model.dto;

import java.time.LocalDate;

public class RequirementResponseDTO {
    private String requirementId;
    private String merchantId;
    private String cropType;
    private Double requiredQuantity;
    private String unit;
    private Double expectedPrice;
    private String district;
    private Double latitude;
    private Double longitude;
    private LocalDate requiredBefore;
    private String description;
    private String status;
    private Double distanceKm;

    public RequirementResponseDTO() {
    }

    public RequirementResponseDTO(String requirementId, String merchantId, String cropType,
                                  Double requiredQuantity, String unit, Double expectedPrice,
                                  String district, Double latitude, Double longitude,
                                  LocalDate requiredBefore, String description, String status) {
        this.requirementId = requirementId;
        this.merchantId = merchantId;
        this.cropType = cropType;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
        this.expectedPrice = expectedPrice;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.requiredBefore = requiredBefore;
        this.description = description;
        this.status = status;
    }

    // Getters and Setters
    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public Double getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Double requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(Double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDate getRequiredBefore() {
        return requiredBefore;
    }

    public void setRequiredBefore(LocalDate requiredBefore) {
        this.requiredBefore = requiredBefore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }
}
