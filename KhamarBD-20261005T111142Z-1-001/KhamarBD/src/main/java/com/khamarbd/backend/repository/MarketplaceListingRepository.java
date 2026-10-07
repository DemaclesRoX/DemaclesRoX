package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.MarketplaceListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketplaceListingRepository extends JpaRepository<MarketplaceListing, Long> {

    List<MarketplaceListing> findByListingTabAndListingStatus(String listingTab, String listingStatus);

    List<MarketplaceListing> findBySeller_UserId(Long sellerId);

    List<MarketplaceListing> findByListingTabAndDivisionAndListingStatus(String listingTab, String division, String listingStatus);

    List<MarketplaceListing> findByListingTabAndDivisionAndDistrictAndListingStatus(String listingTab, String division, String district, String listingStatus);

    List<MarketplaceListing> findByListingTabAndProductCategoryAndListingStatus(String listingTab, String productCategory, String listingStatus);
}
