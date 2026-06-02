package com.nalaka.goviya.service;

import com.nalaka.goviya.model.Harvest;
import com.nalaka.goviya.model.dto.HarvestRequestDTO;
import com.nalaka.goviya.model.dto.HarvestResponseDTO;
import com.nalaka.goviya.repository.HarvestRepository;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HarvestService {
    
    private final HarvestRepository harvestRepository;
    
    public HarvestService(HarvestRepository harvestRepository) {
        this.harvestRepository = harvestRepository;
    }
    
    public Harvest createHarvest(String farmerId, HarvestRequestDTO request) {
        Harvest harvest = new Harvest(
            farmerId,
            request.getTitle(),
            request.getCropType(),
            request.getQuantity(),
            request.getUnit(),
            request.getPricePerUnit(),
            request.getDistrict(),
            request.getLatitude(),
            request.getLongitude(),
            request.getHarvestDate(),
            request.getAvailableUntil(),
            request.getOrganic(),
            request.getDescription()
        );
        
        harvest.setHarvestId("HAR" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        harvest.setStatus("DRAFT");
        harvest.setCreatedAt(LocalDateTime.now());
        harvest.setUpdatedAt(LocalDateTime.now());
        
        return harvestRepository.save(harvest);
    }
    
    public Optional<HarvestResponseDTO> getHarvestById(String harvestId) {
        return harvestRepository.findByHarvestId(harvestId)
            .map(this::convertToResponseDTO);
    }
    
    public Optional<HarvestResponseDTO> getHarvestByMongoId(String mongoId) {
        return harvestRepository.findById(mongoId)
            .map(this::convertToResponseDTO);
    }
    
    public Harvest updateHarvest(String harvestId, HarvestRequestDTO request) {
        Optional<Harvest> optional = harvestRepository.findByHarvestId(harvestId);
        if (optional.isPresent()) {
            Harvest harvest = optional.get();
            harvest.setTitle(request.getTitle());
            harvest.setCropType(request.getCropType());
            harvest.setQuantity(request.getQuantity());
            harvest.setUnit(request.getUnit());
            harvest.setPricePerUnit(request.getPricePerUnit());
            harvest.setDistrict(request.getDistrict());
            harvest.setLatitude(request.getLatitude());
            harvest.setLongitude(request.getLongitude());
            harvest.setLocation(new GeoJsonPoint(request.getLongitude(), request.getLatitude()));
            harvest.setHarvestDate(request.getHarvestDate());
            harvest.setAvailableUntil(request.getAvailableUntil());
            harvest.setOrganic(request.getOrganic());
            harvest.setDescription(request.getDescription());
            harvest.setUpdatedAt(LocalDateTime.now());
            return harvestRepository.save(harvest);
        }
        throw new RuntimeException("Harvest not found with ID: " + harvestId);
    }
    
    public void deleteHarvest(String harvestId) {
        Optional<Harvest> optional = harvestRepository.findByHarvestId(harvestId);
        if (optional.isPresent()) {
            harvestRepository.delete(optional.get());
        } else {
            throw new RuntimeException("Harvest not found with ID: " + harvestId);
        }
    }
    
    public List<HarvestResponseDTO> searchHarvests(String cropType, String district, 
                                                   Double minPrice, Double maxPrice, Boolean organic) {
        List<Harvest> results;
        
        if (cropType != null && minPrice != null && maxPrice != null) {
            results = harvestRepository.findByCropTypeAndPriceRange(cropType, minPrice, maxPrice);
        } else if (cropType != null) {
            results = harvestRepository.findByCropType(cropType);
        } else if (district != null) {
            results = harvestRepository.findByDistrict(district);
        } else if (organic != null) {
            results = harvestRepository.findByOrganic(organic);
        } else {
            results = harvestRepository.findAll();
        }
        
        return results.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    
    public List<HarvestResponseDTO> getHarvestsByFarmerId(String farmerId) {
        return harvestRepository.findByFarmerId(farmerId).stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    public List<HarvestResponseDTO> getHarvestsByStatus(String status) {
        return harvestRepository.findByStatus(status).stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    public List<HarvestResponseDTO> getHarvestsByFarmerIdAndStatus(String farmerId, String status) {
        return harvestRepository.findByFarmerIdAndStatus(farmerId, status).stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    public void updateHarvestStatus(String harvestId, String status) {
        Optional<Harvest> optional = harvestRepository.findByHarvestId(harvestId);
        if (optional.isPresent()) {
            Harvest harvest = optional.get();
            harvest.setStatus(status);
            harvest.setUpdatedAt(LocalDateTime.now());
            harvestRepository.save(harvest);
        } else {
            throw new RuntimeException("Harvest not found with ID: " + harvestId);
        }
    }
    
    private HarvestResponseDTO convertToResponseDTO(Harvest harvest) {
        return new HarvestResponseDTO(
            harvest.getHarvestId(),
            harvest.getFarmerId(),
            harvest.getTitle(),
            harvest.getCropType(),
            harvest.getQuantity(),
            harvest.getUnit(),
            harvest.getPricePerUnit(),
            harvest.getDistrict(),
            harvest.getLatitude(),
            harvest.getLongitude(),
            harvest.getHarvestDate(),
            harvest.getAvailableUntil(),
            harvest.getOrganic(),
            harvest.getDescription(),
            harvest.getStatus()
        );
    }

}
