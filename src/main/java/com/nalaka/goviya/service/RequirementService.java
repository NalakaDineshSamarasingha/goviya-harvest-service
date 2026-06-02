package com.nalaka.goviya.service;

import com.nalaka.goviya.model.Requirement;
import com.nalaka.goviya.model.dto.RequirementRequestDTO;
import com.nalaka.goviya.model.dto.RequirementResponseDTO;
import com.nalaka.goviya.repository.RequirementRepository;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RequirementService {
    
    private final RequirementRepository requirementRepository;
    
    public RequirementService(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }
    
    public Requirement createRequirement(String merchantId, RequirementRequestDTO request) {
        Requirement requirement = new Requirement(
            merchantId,
            request.getCropType(),
            request.getRequiredQuantity(),
            request.getUnit(),
            request.getExpectedPrice(),
            request.getDistrict(),
            request.getLatitude(),
            request.getLongitude(),
            request.getRequiredBefore(),
            request.getDescription()
        );
        
        requirement.setRequirementId("REQ" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        requirement.setStatus("OPEN");
        requirement.setCreatedAt(LocalDateTime.now());
        requirement.setUpdatedAt(LocalDateTime.now());
        
        return requirementRepository.save(requirement);
    }
    
    public Optional<RequirementResponseDTO> getRequirementById(String requirementId) {
        return requirementRepository.findByRequirementId(requirementId)
            .map(this::convertToResponseDTO);
    }
    
    public Optional<RequirementResponseDTO> getRequirementByMongoId(String mongoId) {
        return requirementRepository.findById(mongoId)
            .map(this::convertToResponseDTO);
    }
    
    public Requirement updateRequirement(String requirementId, RequirementRequestDTO request) {
        Optional<Requirement> optional = requirementRepository.findByRequirementId(requirementId);
        if (optional.isPresent()) {
            Requirement requirement = optional.get();
            requirement.setCropType(request.getCropType());
            requirement.setRequiredQuantity(request.getRequiredQuantity());
            requirement.setUnit(request.getUnit());
            requirement.setExpectedPrice(request.getExpectedPrice());
            requirement.setDistrict(request.getDistrict());
            requirement.setLatitude(request.getLatitude());
            requirement.setLongitude(request.getLongitude());
            requirement.setLocation(new GeoJsonPoint(request.getLongitude(), request.getLatitude()));
            requirement.setRequiredBefore(request.getRequiredBefore());
            requirement.setDescription(request.getDescription());
            requirement.setUpdatedAt(LocalDateTime.now());
            return requirementRepository.save(requirement);
        }
        throw new RuntimeException("Requirement not found with ID: " + requirementId);
    }
    
    public void deleteRequirement(String requirementId) {
        Optional<Requirement> optional = requirementRepository.findByRequirementId(requirementId);
        if (optional.isPresent()) {
            requirementRepository.delete(optional.get());
        } else {
            throw new RuntimeException("Requirement not found with ID: " + requirementId);
        }
    }

    
    public List<RequirementResponseDTO> getRequirementsByMerchantId(String merchantId) {
        return requirementRepository.findByMerchantId(merchantId).stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    public void updateRequirementStatus(String requirementId, String status) {
        Optional<Requirement> optional = requirementRepository.findByRequirementId(requirementId);
        if (optional.isPresent()) {
            Requirement requirement = optional.get();
            requirement.setStatus(status);
            requirement.setUpdatedAt(LocalDateTime.now());
            requirementRepository.save(requirement);
        } else {
            throw new RuntimeException("Requirement not found with ID: " + requirementId);
        }
    }

    public List<RequirementResponseDTO> searchRequirements() {
        return requirementRepository.findAll().stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }
    
    private RequirementResponseDTO convertToResponseDTO(Requirement requirement) {
        return new RequirementResponseDTO(
            requirement.getRequirementId(),
            requirement.getMerchantId(),
            requirement.getCropType(),
            requirement.getRequiredQuantity(),
            requirement.getUnit(),
            requirement.getExpectedPrice(),
            requirement.getDistrict(),
            requirement.getLatitude(),
            requirement.getLongitude(),
            requirement.getRequiredBefore(),
            requirement.getDescription(),
            requirement.getStatus()
        );
    }

}
