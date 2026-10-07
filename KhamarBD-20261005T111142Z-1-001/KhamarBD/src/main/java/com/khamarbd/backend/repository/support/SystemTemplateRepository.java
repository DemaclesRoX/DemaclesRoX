package com.khamarbd.backend.repository.support;

import com.khamarbd.backend.entity.support.SystemTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface SystemTemplateRepository extends JpaRepository<SystemTemplate, String> {

    List<SystemTemplate> findByTemplateType(String templateType);

    List<SystemTemplate> findBySector(String sector);

    List<SystemTemplate> findByTemplateTypeAndSector(String templateType, String sector);

    
    @Query("SELECT t FROM SystemTemplate t WHERE t.templateType = :type AND (t.ownerUserId IS NULL OR t.ownerUserId = :userId)")
    List<SystemTemplate> findByTypeAndUserIdForFarmer(@Param("type") String type, @Param("userId") Long userId);

    List<SystemTemplate> findByTemplateTypeAndSectorAndVariety(String templateType, String sector, String variety);
}

