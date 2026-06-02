package com.nalaka.goviya.repository;

import com.nalaka.goviya.model.Requirement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequirementRepository extends MongoRepository<Requirement, String> {
    
    Optional<Requirement> findByRequirementId(String requirementId);
    
    List<Requirement> findByMerchantId(String merchantId);
    
    List<Requirement> findByStatus(String status);
    
    List<Requirement> findByCropType(String cropType);
    
    List<Requirement> findByDistrict(String district);
}
