package com.nalaka.goviya.controller;

import com.nalaka.goviya.model.Requirement;
import com.nalaka.goviya.model.dto.ApiResponseDTO;
import com.nalaka.goviya.model.dto.RequirementRequestDTO;
import com.nalaka.goviya.model.dto.RequirementResponseDTO;
import com.nalaka.goviya.service.RequirementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/requirements")
@Tag(name = "Requirement Management", description = "APIs for merchant requirement management")
public class RequirementController {
    
    private final RequirementService requirementService;
    
    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new requirement", description = "Create a new merchant requirement")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> createRequirement(
            @RequestHeader("X-User-Id") String merchantId,
            @RequestBody RequirementRequestDTO request) {
        try {
            Requirement requirement = requirementService.createRequirement(merchantId, request);
            Map<String, String> data = new HashMap<>();
            data.put("requirementId", requirement.getRequirementId());
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Requirement created successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO<>(false, "Failed to create requirement: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/{requirementId}")
    @Operation(summary = "Get requirement by ID", description = "Retrieve a requirement by its requirement ID")
    public ResponseEntity<ApiResponseDTO<RequirementResponseDTO>> getRequirement(@PathVariable String requirementId) {
        try {
            Optional<RequirementResponseDTO> requirement = requirementService.getRequirementById(requirementId);
            if (requirement.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, "Requirement retrieved successfully", requirement.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Requirement not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error retrieving requirement: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{requirementId}")
    @Operation(summary = "Update requirement", description = "Update an existing requirement")
    public ResponseEntity<ApiResponseDTO<RequirementResponseDTO>> updateRequirement(
            @PathVariable String requirementId,
            @RequestBody RequirementRequestDTO request) {
        try {
            Requirement updatedRequirement = requirementService.updateRequirement(requirementId, request);
            Optional<RequirementResponseDTO> response = requirementService.getRequirementByMongoId(updatedRequirement.getId());
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Requirement updated successfully", 
                response.orElse(null)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error updating requirement: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{requirementId}")
    @Operation(summary = "Delete requirement", description = "Delete a merchant requirement")
    public ResponseEntity<ApiResponseDTO<Void>> deleteRequirement(@PathVariable String requirementId) {
        try {
            requirementService.deleteRequirement(requirementId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Requirement deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error deleting requirement: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/nearby")
    @Operation(summary = "Search nearby requirements", description = "Search for merchant requirements near a specific location")
    public ResponseEntity<ApiResponseDTO<List<RequirementResponseDTO>>> searchNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "25") Integer radiusKm) {
        try {
            List<RequirementResponseDTO> results = requirementService.searchNearby(latitude, longitude, radiusKm);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Nearby search completed successfully", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error searching nearby requirements: " + e.getMessage(), null));
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search requirements", description = "Get all requirements")
    public ResponseEntity<ApiResponseDTO<List<RequirementResponseDTO>>> searchRequirements() {
        try {
            List<RequirementResponseDTO> results = requirementService.searchRequirements();
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Search completed successfully", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error searching requirements: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/merchant/{merchantId}")
    @Operation(summary = "Get merchant's requirements", description = "Retrieve all requirements for a specific merchant")
    public ResponseEntity<ApiResponseDTO<List<RequirementResponseDTO>>> getMerchantRequirements(@PathVariable String merchantId) {
        try {
            List<RequirementResponseDTO> results = requirementService.getRequirementsByMerchantId(merchantId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Merchant requirements retrieved successfully", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error retrieving merchant requirements: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{requirementId}/status")
    @Operation(summary = "Update requirement status", description = "Update the status of a requirement")
    public ResponseEntity<ApiResponseDTO<Void>> updateRequirementStatus(
            @PathVariable String requirementId,
            @RequestParam String status) {
        try {
            requirementService.updateRequirementStatus(requirementId, status);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Requirement status updated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error updating requirement status: " + e.getMessage(), null));
        }
    }
}
