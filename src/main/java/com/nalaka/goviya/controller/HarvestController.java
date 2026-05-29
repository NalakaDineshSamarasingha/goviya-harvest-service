package com.nalaka.goviya.controller;

import com.nalaka.goviya.model.Harvest;
import com.nalaka.goviya.model.dto.ApiResponseDTO;
import com.nalaka.goviya.model.dto.HarvestRequestDTO;
import com.nalaka.goviya.model.dto.HarvestResponseDTO;
import com.nalaka.goviya.service.HarvestService;
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
@RequestMapping("/api/v1/harvests")
@Tag(name = "Harvest Management", description = "APIs for harvest listing and management")
public class HarvestController {
    
    private final HarvestService harvestService;
    
    public HarvestController(HarvestService harvestService) {
        this.harvestService = harvestService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new harvest", description = "Create a new harvest listing")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> createHarvest(
            @RequestHeader("X-User-Id") String farmerId,
            @RequestBody HarvestRequestDTO request) {
        try {
            Harvest harvest = harvestService.createHarvest(farmerId, request);
            Map<String, String> data = new HashMap<>();
            data.put("harvestId", harvest.getHarvestId());
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(true, "Harvest created successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO<>(false, "Failed to create harvest: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/{harvestId}")
    @Operation(summary = "Get harvest by ID", description = "Retrieve a harvest by its harvest ID")
    public ResponseEntity<ApiResponseDTO<HarvestResponseDTO>> getHarvest(@PathVariable String harvestId) {
        try {
            Optional<HarvestResponseDTO> harvest = harvestService.getHarvestById(harvestId);
            if (harvest.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO<>(true, "Harvest retrieved successfully", harvest.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>(false, "Harvest not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error retrieving harvest: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{harvestId}")
    @Operation(summary = "Update harvest", description = "Update an existing harvest")
    public ResponseEntity<ApiResponseDTO<HarvestResponseDTO>> updateHarvest(
            @PathVariable String harvestId,
            @RequestBody HarvestRequestDTO request) {
        try {
            Harvest updatedHarvest = harvestService.updateHarvest(harvestId, request);
            Optional<HarvestResponseDTO> response = harvestService.getHarvestByMongoId(updatedHarvest.getId());
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Harvest updated successfully", 
                response.orElse(null)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error updating harvest: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{harvestId}")
    @Operation(summary = "Delete harvest", description = "Delete a harvest listing")
    public ResponseEntity<ApiResponseDTO<Void>> deleteHarvest(@PathVariable String harvestId) {
        try {
            harvestService.deleteHarvest(harvestId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Harvest deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error deleting harvest: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search harvests", description = "Search harvests by crop type, district, price range, or organic status")
    public ResponseEntity<ApiResponseDTO<List<HarvestResponseDTO>>> searchHarvests(
            @RequestParam(required = false) String cropType,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean organic) {
        try {
            List<HarvestResponseDTO> results = harvestService.searchHarvests(cropType, district, minPrice, maxPrice, organic);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Search completed successfully", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error searching harvests: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/nearby")
    @Operation(summary = "Search nearby harvests", description = "Search for harvests near a specific location")
    public ResponseEntity<ApiResponseDTO<List<HarvestResponseDTO>>> searchNearby(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "25") Integer radiusKm,
            @RequestParam(required = false) String cropType) {
        try {
            List<HarvestResponseDTO> results = harvestService.searchNearby(latitude, longitude, radiusKm, cropType);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Nearby search completed successfully", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error searching nearby harvests: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/farmer/{farmerId}")
    @Operation(summary = "Get farmer's harvests", description = "Retrieve all harvests for a specific farmer")
    public ResponseEntity<ApiResponseDTO<List<HarvestResponseDTO>>> getFarmerHarvests(@PathVariable String farmerId) {
        try {
            List<HarvestResponseDTO> results = harvestService.getHarvestsByFarmerId(farmerId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Farmer harvests retrieved successfully", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error retrieving farmer harvests: " + e.getMessage(), null));
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get harvests by status", description = "Retrieve all harvests for a specific status")
    public ResponseEntity<ApiResponseDTO<List<HarvestResponseDTO>>> getHarvestsByStatus(@PathVariable String status) {
        try {
            List<HarvestResponseDTO> results = harvestService.getHarvestsByStatus(status);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Harvests retrieved successfully", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error retrieving harvests by status: " + e.getMessage(), null));
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user's harvests", description = "Retrieve harvests for the authenticated user using X-User-Id")
    public ResponseEntity<ApiResponseDTO<List<HarvestResponseDTO>>> getMyHarvests(
            @RequestHeader("X-User-Id") String farmerId,
            @RequestParam(required = false) String status) {
        try {
            List<HarvestResponseDTO> results = status != null && !status.isBlank()
                ? harvestService.getHarvestsByFarmerIdAndStatus(farmerId, status)
                : harvestService.getHarvestsByFarmerId(farmerId);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Harvests retrieved successfully", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error retrieving user harvests: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{harvestId}/status")
    @Operation(summary = "Update harvest status", description = "Update the status of a harvest")
    public ResponseEntity<ApiResponseDTO<Void>> updateHarvestStatus(
            @PathVariable String harvestId,
            @RequestParam String status) {
        try {
            harvestService.updateHarvestStatus(harvestId, status);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Harvest status updated successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>(false, "Error updating harvest status: " + e.getMessage(), null));
        }
    }
}
