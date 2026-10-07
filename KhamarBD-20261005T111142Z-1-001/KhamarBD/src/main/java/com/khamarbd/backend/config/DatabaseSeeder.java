package com.khamarbd.backend.config;

import com.khamarbd.backend.entity.support.SystemTemplate;
import com.khamarbd.backend.repository.support.SystemTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.khamarbd.backend.entity.MarketplaceListing;
import com.khamarbd.backend.repository.MarketplaceListingRepository;
import java.math.BigDecimal;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private SystemTemplateRepository templateRepo;

    @Autowired
    private MarketplaceListingRepository listingRepo;

    @Autowired
    private com.khamarbd.backend.repository.UserRepository userRepo;

    @Autowired
    private com.khamarbd.backend.repository.OrderTransactionRepository orderRepo;

    @Override
    public void run(String... args) throws Exception {
        if (templateRepo.count() == 0) {
            seedTemplates();
        }
        if (listingRepo.count() == 0) {
            seedMarketplace();
        }
        if (orderRepo.count() == 0) {
            seedOrders();
        }
    }

    private void seedTemplates() {
        saveTemplate("broiler-35", "ROUTINE", "POULTRY", 35, "Broiler Meat Batch (35-Day)", "[" +
                "{\"dayOffset\": 1, \"taskTitle\": \"Chick receiving & Brooder setup (32-34°C)\", \"actionType\": \"Housing/Environment\"}," +
                "{\"dayOffset\": 1, \"taskTitle\": \"Newcastle+IB (ND+IB) spray/eye-drop\", \"actionType\": \"Vaccination\"}," +
                "{\"dayOffset\": 7, \"taskTitle\": \"Weekly weight sampling (5% birds)\", \"actionType\": \"Weighing\"}," +
                "{\"dayOffset\": 11, \"taskTitle\": \"Gumboro (IBD) intermediate vaccine\", \"actionType\": \"Vaccination\"}," +
                "{\"dayOffset\": 15, \"taskTitle\": \"Litter turning & dry patches\", \"actionType\": \"Sanitization\"}," +
                "{\"dayOffset\": 21, \"taskTitle\": \"Newcastle booster in drinking water\", \"actionType\": \"Vaccination\"}," +
                "{\"dayOffset\": 25, \"taskTitle\": \"Shift to Finisher feed\", \"actionType\": \"Feeding\"}" +
                "{\"dayOffset\": 35, \"taskTitle\": \"Final weight & harvest preparation\", \"actionType\": \"Harvesting\"}" +
                "]"
        );

        saveTemplate("sonali-layer", "ROUTINE", "POULTRY", 56, "Sonali 56-Day", "[" +
                "{\"dayOffset\": 1, \"taskTitle\": \"Day 1 Care & Electrolytes\", \"actionType\": \"CARE\"}," +
                "{\"dayOffset\": 21, \"taskTitle\": \"Intermediate Vaccination\", \"actionType\": \"VACCINATION\"}," +
                "{\"dayOffset\": 45, \"taskTitle\": \"Weight Sampling\", \"actionType\": \"MONITORING\"}" +
                "]"
        );

        saveTemplate("carp-poly", "ROUTINE", "FISHERIES", 240, "Carp Polyculture", "[" +
                "{\"dayOffset\": 1, \"taskTitle\": \"Pond Preparation & Lime Application\", \"actionType\": \"MAINTENANCE\"}," +
                "{\"dayOffset\": 30, \"taskTitle\": \"Water Quality Test (pH & Ammonia)\", \"actionType\": \"MONITORING\"}," +
                "{\"dayOffset\": 120, \"taskTitle\": \"Mid-cycle Netting & Health Check\", \"actionType\": \"MONITORING\"}" +
                "]"
        );

        saveTemplate("dairy-cow", "ROUTINE", "LIVESTOCK", 90, "Dairy Cow 90-Day", "[" +
                "{\"dayOffset\": 1, \"taskTitle\": \"Deworming & Checkup\", \"actionType\": \"MEDICATION\"}," +
                "{\"dayOffset\": 15, \"taskTitle\": \"Adjust Concentrate Feed Ratio\", \"actionType\": \"FEEDING\"}," +
                "{\"dayOffset\": 60, \"taskTitle\": \"Pregnancy/Heat Monitoring\", \"actionType\": \"MONITORING\"}" +
                "]"
        );

        saveTemplate("rice-boro", "ROUTINE", "AGRICULTURE", 120, "Boro Rice (120-Day)", "[" +
                "{\"dayOffset\": 1, \"taskTitle\": \"Land preparation (Puddling & Basal dose)\", \"actionType\": \"Fertilizing\"}," +
                "{\"dayOffset\": 3, \"taskTitle\": \"Transplanting (line-sowing 20x15cm)\", \"actionType\": \"Planting\"}," +
                "{\"dayOffset\": 15, \"taskTitle\": \"First top dressing (Urea) & Weeding\", \"actionType\": \"Fertilizing\"}," +
                "{\"dayOffset\": 35, \"taskTitle\": \"AWD (Alternate Wetting and Drying) irrigation start\", \"actionType\": \"Irrigation\"}," +
                "{\"dayOffset\": 55, \"taskTitle\": \"Booting stage: Final Urea & Potash\", \"actionType\": \"Fertilizing\"}," +
                "{\"dayOffset\": 75, \"taskTitle\": \"Stem borer / BPH field scouting\", \"actionType\": \"Monitoring\"}," +
                "{\"dayOffset\": 110, \"taskTitle\": \"Drain field 10 days before harvest\", \"actionType\": \"Irrigation\"}," +
                "{\"dayOffset\": 120, \"taskTitle\": \"Harvest (80% grain golden)\", \"actionType\": \"Harvesting\"}" +
                "]"
        );

        // PRESCRIPTION TEMPLATES
                saveTemplate("rx-broiler-standard", "PRESCRIPTION", "POULTRY", 35, "Broiler Standard Medication Protocol", "[" +
                "{\"medicineName\": \"Gumboro Live Vaccine (IBD)\", \"dosage\": \"1 drop per eye\", \"durationDays\": 1, \"instructions\": \"Administer on Day 12-14\"}," +
                "{\"medicineName\": \"Electrolyte & Vit-C Powder\", \"dosage\": \"1g / Liter Water\", \"durationDays\": 3, \"instructions\": \"Mix in morning water to reduce heat stress\"}," +
                "{\"medicineName\": \"Amoxicillin Trihydrate\", \"dosage\": \"1g / 2 Liters Water\", \"durationDays\": 5, \"instructions\": \"Preventive antibiotic during weather change\"}" +
                "]"
        );

        saveTemplate("rx-layer-egg", "PRESCRIPTION", "POULTRY", 30, "Layer Egg Drop Syndrome Care", "[" +
                "{\"medicineName\": \"Vitamin AD3E Liquid\", \"dosage\": \"5ml / 100 Birds\", \"durationDays\": 7, \"instructions\": \"Mix in drinking water\"}" +
                "]"
        );

        saveTemplate("rx-mastitis", "PRESCRIPTION", "DAIRY", 10, "Clinical Mastitis Treatment", "[" +
                "{\"medicineName\": \"Ceftiofur Sodium Injection\", \"dosage\": \"1mg / kg Body wt\", \"durationDays\": 5, \"instructions\": \"IM Injection (Withdrawal: Milk 72h)\"}," +
                "{\"medicineName\": \"Flunixin Meglumine (Anti-inflammatory)\", \"dosage\": \"2ml / 45kg Body wt\", \"durationDays\": 3, \"instructions\": \"IV/IM Injection\"}" +
                "]"
        );

        saveTemplate("rx-fmd", "PRESCRIPTION", "DAIRY", 15, "Foot & Mouth Disease (FMD) Care", "[" +
                "{\"medicineName\": \"Oxytetracycline LA\", \"dosage\": \"1ml / 10kg Body wt\", \"durationDays\": 1, \"instructions\": \"Deep IM (Secondary prevention)\"}," +
                "{\"medicineName\": \"KMnO4 Wash\", \"dosage\": \"0.1% solution\", \"durationDays\": 7, \"instructions\": \"Wash foot lesions twice daily\"}" +
                "]"
        );

        saveTemplate("rx-ammonia", "PRESCRIPTION", "FISHERIES", 5, "High Ammonia & Gas Crisis", "[" +
                "{\"medicineName\": \"Zeolite + Yucca extract\", \"dosage\": \"1kg / decimal\", \"durationDays\": 1, \"instructions\": \"Broadcast evenly over pond\"}" +
                "]"
        );
    }

    private void saveTemplate(String code, String type, String sector, int days, String name, String json) {
        SystemTemplate t = new SystemTemplate();
        t.setTemplateCode(code);
        t.setTemplateType(type);
        t.setSector(sector);
        t.setDurationDays(days);
        t.setNameEn(name);
        t.setContentJson(json);
        templateRepo.save(t);
    }

    private void seedMarketplace() {
        com.khamarbd.backend.entity.User seller = userRepo.findById(2L).orElse(null);
        if (seller == null && userRepo.count() > 0) {
            seller = userRepo.findAll().get(0);
        }

        saveListing(seller, "Sonali Chick Starter Crumble (50kg Bag)", "FEED", "Farm Supplies", "POULTRY", 2750.0, 48, "50kg Bag", "Nourish Feeds", "Rajshahi", "Bogura", "Sadar");
        saveListing(seller, "Renamycin LA 100ml Injectable Solution", "MEDICINE", "Farm Supplies", "DAIRY_LIVESTOCK", 180.0, 25, "100ml Bottle", "Renata Ltd", "Rajshahi", "Bogura", "Sadar");
        saveListing(seller, "High Grade Agricultural Slaked Lime", "FERTILIZER", "Farm Supplies", "FISHERIES", 450.0, 12, "25kg Sack", "AgroCare", "Rajshahi", "Bogura", "Sadar");
        saveListing(seller, "Broiler Plastic Bell Drinker (Automatic)", "EQUIPMENT", "Farm Supplies", "POULTRY", 320.0, 30, "Piece", "HeavyDuty", "Rajshahi", "Bogura", "Sadar");
        saveListing(seller, "Live Broiler Birds (Avg 1.8kg)", "POULTRY", "Farm Products", "POULTRY", 165.0, 450, "Kg", "Bogura Broiler Project", "Rajshahi", "Bogura", "Shibganj");
    }

    private void saveListing(com.khamarbd.backend.entity.User seller, String title, String category, String tab, String sector, double price, int qty, String unit, String brand, String div, String dist, String upazila) {
        MarketplaceListing listing = new MarketplaceListing();
        listing.setSeller(seller);
        listing.setTitle(title);
        listing.setProductCategory(category);
        listing.setListingTab(tab);
        listing.setFarmSector(sector);
        listing.setPrice(BigDecimal.valueOf(price));
        listing.setQuantityAvailable(BigDecimal.valueOf(qty));
        listing.setUnit(unit);
        listing.setBrandName(brand);
        listing.setDivision(div);
        listing.setDistrict(dist);
        listing.setUpazila(upazila);
        listing.setListingStatus("ACTIVE");
        listingRepo.save(listing);
    }

    private void seedOrders() {
        com.khamarbd.backend.entity.User buyer = userRepo.findById(1L).orElse(null);
        com.khamarbd.backend.entity.User seller = userRepo.findById(2L).orElse(null);
        java.util.List<MarketplaceListing> listings = listingRepo.findAll();

        if (!listings.isEmpty()) {
            MarketplaceListing item1 = listings.get(0);
            com.khamarbd.backend.entity.OrderTransaction ord1 = new com.khamarbd.backend.entity.OrderTransaction();
            ord1.setBuyer(buyer);
            ord1.setListing(item1);
            ord1.setSellerId(item1.getSeller() != null ? item1.getSeller().getUserId() : (seller != null ? seller.getUserId() : 2L));
            ord1.setQuantity(BigDecimal.valueOf(2));
            ord1.setTotalAgreedPrice(item1.getPrice().multiply(BigDecimal.valueOf(2)));
            ord1.setOrderStatus("ORDER_RECEIVED");
            ord1.setOrderReference("ORD-1024");
            ord1.setPaymentNote("Station Road branch counter collection");
            orderRepo.save(ord1);

            if (listings.size() > 1) {
                MarketplaceListing item2 = listings.get(1);
                com.khamarbd.backend.entity.OrderTransaction ord2 = new com.khamarbd.backend.entity.OrderTransaction();
                ord2.setBuyer(buyer);
                ord2.setListing(item2);
                ord2.setSellerId(item2.getSeller() != null ? item2.getSeller().getUserId() : (seller != null ? seller.getUserId() : 2L));
                ord2.setQuantity(BigDecimal.valueOf(1));
                ord2.setTotalAgreedPrice(item2.getPrice());
                ord2.setOrderStatus("IN_TRANSPORT");
                ord2.setOrderReference("ORD-1025");
                ord2.setPaymentNote("Delivery van en route to Bogura Broiler Project (Driver: 01712-345678)");
                orderRepo.save(ord2);
            }
        }
    }
}


