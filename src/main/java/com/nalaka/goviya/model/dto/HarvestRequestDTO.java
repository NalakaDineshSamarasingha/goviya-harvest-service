package com.nalaka.goviya.model.dto;

import java.time.LocalDate;

public class HarvestRequestDTO {
    private String title;
    private String cropType;
    private Double quantity;
    private String unit;
    private Double pricePerUnit;
    private String district;
    private Double latitude;
    private Double longitude;
    private LocalDate harvestDate;
    private LocalDate availableUntil;
    private Boolean organic;
    private String description;

    public HarvestRequestDTO() {
    }

    public HarvestRequestDTO(String title, String cropType, Double quantity, String unit,
                             Double pricePerUnit, String district, Double latitude, Double longitude,
                             LocalDate harvestDate, LocalDate availableUntil, Boolean organic, String description) {
        this.title = title;
        this.cropType = cropType;
        this.quantity = quantity;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.harvestDate = harvestDate;
        this.availableUntil = availableUntil;
        this.organic = organic;
        this.description = description;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
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

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public LocalDate getAvailableUntil() {
        return availableUntil;
    }

    public void setAvailableUntil(LocalDate availableUntil) {
        this.availableUntil = availableUntil;
    }

    public Boolean getOrganic() {
        return organic;
    }

    public void setOrganic(Boolean organic) {
        this.organic = organic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
