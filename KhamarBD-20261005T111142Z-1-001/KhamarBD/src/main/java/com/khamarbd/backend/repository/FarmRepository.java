package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {

    List<Farm> findByUser_UserId(Long userId);

    @Query("SELECT f FROM Farm f WHERE f.farmType = :sector")
    List<Farm> findBySector(@Param("sector") String sector);

    List<Farm> findByFarmType(String farmType);

    List<Farm> findByDivisionAndDistrict(String division, String district);
}
