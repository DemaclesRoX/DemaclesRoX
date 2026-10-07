package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.FarmRequestDto;
import com.khamarbd.backend.entity.Farm;
import com.khamarbd.backend.entity.User;
import com.khamarbd.backend.repository.FarmRepository;
import com.khamarbd.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmService {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private UserRepository userRepository;

    public Farm saveFarm(FarmRequestDto dto) {
        Farm farm = new Farm();
        User user = userRepository.findById(dto.getUserId()).orElse(null);
        farm.setUser(user);
        farm.setName(dto.getFarmName());
        farm.setSector(dto.getFarmType());
        farm.setVillage(dto.getLocationDetails());

        // FIX (blocker): Farm.division / district / upazila are nullable = false in the
        // entity, but this method never set them, so every POST /farm/create ended in a
        // DataIntegrityViolationException -> 500 Internal Server Error.
        // Rule used here: a farm inherits the location of its owner. This is the same
        // approach MarketplaceListingService already uses for its own
        // division/district/upazila columns, so the codebases stay consistent.
        if (user != null) {
            farm.setDivision(user.getDivision());
            farm.setDistrict(user.getDistrict());
            farm.setUpazila(user.getUpazila());
        }

        return farmRepository.save(farm);
    }

    public List<Farm> getAllFarms() {
        return farmRepository.findAll();
    }

    public Farm getFarmById(Long id) {
        return farmRepository.findById(id).orElse(null);
    }

    public List<Farm> getFarmsByUserId(Long userId) {
        return farmRepository.findByUser_UserId(userId);
    }

    public List<Farm> getFarmsBySector(String sector) {
        return farmRepository.findBySector(sector);
    }
}
