package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.LotOrAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotOrAnimalRepository extends JpaRepository<LotOrAnimal, Long> {

    List<LotOrAnimal> findByFarm_FarmId(Long farmId);

    List<LotOrAnimal> findByFarm_FarmIdAndStatus(Long farmId, String status);

    List<LotOrAnimal> findByFarm_User_UserId(Long userId);

    List<LotOrAnimal> findByFarm_User_UserIdAndStatus(Long userId, String status);
}
