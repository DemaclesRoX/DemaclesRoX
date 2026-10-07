package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.MarketplaceListingRequestDto;
import com.khamarbd.backend.entity.LotOrAnimal;
import com.khamarbd.backend.entity.MarketplaceListing;
import com.khamarbd.backend.entity.User;
import com.khamarbd.backend.repository.LotOrAnimalRepository;
import com.khamarbd.backend.repository.MarketplaceListingRepository;
import com.khamarbd.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MarketplaceListingService {

    @Autowired
    private MarketplaceListingRepository marketplaceListingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LotOrAnimalRepository lotOrAnimalRepository;

    public MarketplaceListing saveListing(MarketplaceListingRequestDto dto) {
        MarketplaceListing listing = new MarketplaceListing();
        User seller = userRepository.findById(dto.getSellerId()).orElse(null);
        listing.setSeller(seller);
        if (dto.getLotId() != null) {
            LotOrAnimal lot = lotOrAnimalRepository.findById(dto.getLotId()).orElse(null);
            listing.setLotOrAnimal(lot);
        }
        listing.setTitle(dto.getTitle());
        listing.setProductCategory(dto.getProductCategory());
        listing.setPrice(BigDecimal.valueOf(dto.getPrice()));
        listing.setQuantityAvailable(BigDecimal.valueOf(dto.getQuantityAvailable()));
        listing.setUnit(dto.getUnit());

        // FIX: listingTab used to be hard-coded to "Farm Products", so
        // GET /listing/search?listingTab=FARM_PRODUCTS never matched anything and always
        // returned an empty array. listingTab is nullable = false, so it still has to be
        // given a value — the tab is derived from the product category instead. A supplier
        // selling "Farm Supplies" (seed, feed, medicine...) lands on the Supplies tab.
        listing.setFarmSector(dto.getFarmSector());
        listing.setListingTab(classifyListingTab(dto.getProductCategory()));

        listing.setBrandName(dto.getBrandName());
        listing.setDescription(dto.getDescription());
        if (dto.getImageUrls() != null && !dto.getImageUrls().isBlank()) {
            String raw = dto.getImageUrls().trim();
            if ((raw.startsWith("[") && raw.endsWith("]")) || (raw.startsWith("{") && raw.endsWith("}"))) {
                listing.setImageUrls(raw);
            } else {
                String escaped = raw.replace("\\", "\\\\").replace("\"", "\\\"");
                listing.setImageUrls("[\"" + escaped + "\"]");
            }
        } else {
            listing.setImageUrls(null);
        }

        if (seller != null && (dto.getDivision() == null || dto.getDivision().isBlank())) {
            listing.setDivision(seller.getDivision());
            listing.setDistrict(seller.getDistrict());
            listing.setUpazila(seller.getUpazila());
        } else {
            listing.setDivision(dto.getDivision() != null ? dto.getDivision() : "Dhaka");
            listing.setDistrict(dto.getDistrict() != null ? dto.getDistrict() : "Dhaka");
            listing.setUpazila(dto.getUpazila() != null ? dto.getUpazila() : "Sadar");
        }
        
        return marketplaceListingRepository.save(listing);
    }

    // Maps product category onto Farm Products vs Farm Supplies
    private String classifyListingTab(String productCategory) {
        if (productCategory == null || productCategory.isBlank()) {
            return "Farm Products";
        }
        String category = productCategory.trim().toUpperCase();
        if (category.contains("FEED") || category.contains("MEDICINE") || category.contains("VACCINE")
                || category.contains("FERTILIZER") || category.contains("SEED") || category.contains("EQUIPMENT")
                || category.contains("SUPPLIES") || category.contains("SUPPLY")) {
            return "Farm Supplies";
        }
        return "Farm Products";
    }

    public List<MarketplaceListing> getAllListings() {
        return marketplaceListingRepository.findAll();
    }

    public MarketplaceListing getListingById(Long id) {
        return marketplaceListingRepository.findById(id).orElse(null);
    }

    public List<MarketplaceListing> getListingsByTab(String tab) {
        return marketplaceListingRepository.findByListingTabAndListingStatus(tab, "ACTIVE");
    }

    public List<MarketplaceListing> getListingsBySellerId(Long sellerId) {
        return marketplaceListingRepository.findBySeller_UserId(sellerId);
    }

    public boolean deleteListing(Long id) {
        if (marketplaceListingRepository.existsById(id)) {
            marketplaceListingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public MarketplaceListing updateListingStatus(Long id, String status) {
        MarketplaceListing listing = marketplaceListingRepository.findById(id).orElse(null);
        if (listing != null) {
            listing.setListingStatus(status);
            return marketplaceListingRepository.save(listing);
        }
        return null;
    }
}

