package com.khamarbd.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end smoke test that replays the exact request bodies stored in
 * thunder-tests/KhamarBD_Collection.json, in the same order.
 *
 * Why this test exists: the old Thunder collection sent field names the DTOs do not
 * have (fullName, mobile, subscriptionTier, unitName, quantity, status ...), so no
 * request was ever validated end to end. This test proves, inside a normal Gradle
 * build and without starting a server by hand, that:
 *
 *   1. every POST returns 200 - no 400 from bean validation, no 500 from the database
 *      (Farm.division/district/upazila and LotOrAnimal.unitKind are nullable = false
 *      and used to be left unset, which made every create fail),
 *   2. GET /farm/search and GET /farm/filter/{id}/details no longer explode with a
 *      Jackson circular reference (Farm <-> LotOrAnimal <-> FarmLedger recursion),
 *   3. POST /consultation/create accepts a plain "2026-10-05" date instead of
 *      throwing from ZonedDateTime.parse(...).
 *
 * MockMvc talks straight to the DispatcherServlet, so no port 8080 is needed.
 * Note: Spring Boot 4 removed TestRestTemplate in favour of RestTestClient, so this
 * test uses classic MockMvc, which has no deprecated parts.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KhamarBdApiSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String NEW_USER = "{\"name\":\"%s\",\"phone\":\"%s\","
            + "\"email\":\"%s\",\"password\":\"123456\",\"primaryRole\":\"%s\","
            + "\"division\":\"Dhaka\",\"district\":\"Dhaka\",\"upazila\":\"Savar\"}";

    private static String user(String name, String phone, String email, String role) {
        return String.format(NEW_USER, name, phone, email, role);
    }

    @Test
    @Order(1)
    void step1_registerAllFourRoles() throws Exception {
        mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
                        .content(user("Rahim Uddin", "01700000001", "rahim@khamarbd.test", "FARMER")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
                        .content(user("Dr. Karim Ali", "01700000002", "karim@khamarbd.test", "SPECIALIST")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
                        .content(user("Nabin Agro Store", "01700000003", "nabin@khamarbd.test", "SUPPLIER")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
                        .content(user("Salma Begum", "01700000004", "salma@khamarbd.test", "BUYER")))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void step2_searchUsersAndReadProfile() throws Exception {
        mockMvc.perform(get("/user/search").param("primaryRole", "FARMER"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Rahim Uddin")));

        mockMvc.perform(get("/user/filter/1/profile"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void step3_createFarm_thenReadsBackWithoutCircularReference() throws Exception {
        // farmType must match FarmRequestDto's @Pattern(Livestock|Fisheries|Crops),
        // otherwise bean validation answers 400 instead of reaching the database.
        mockMvc.perform(post("/farm/create").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"farmName\":\"Rahim Poultry Farm\","
                                + "\"farmType\":\"Livestock\",\"locationDetails\":\"Baroipara, Savar\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("farmId")));

        // These two GETs are the ones the @JsonIgnore fix protects: without it Jackson
        // follows Farm -> lotsOrAnimals -> LotOrAnimal -> farm -> ... forever.
        mockMvc.perform(get("/farm/search").param("farmType", "Livestock"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/farm/filter/1/details"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Rahim Poultry Farm")));
    }

    @Test
    @Order(4)
    void step4_createLot_thenReadsBack() throws Exception {
        // category must be Individual|Lot per LotOrAnimalRequestDto.
        mockMvc.perform(post("/lot/create").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"farmId\":1,\"identifierTag\":\"BROILER-A\",\"category\":\"Lot\","
                                + "\"speciesOrBreed\":\"Broiler\",\"startDate\":\"2026-09-01\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/lot/search").param("farmId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void step5_createConsultationWithPlainDate() throws Exception {
        // Exactly the payload from the collection. Previously this reached
        // ZonedDateTime.parse("2026-10-05") and returned 500.
        mockMvc.perform(post("/consultation/create").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"farmerId\":1,\"specialistId\":2,\"lotId\":1,"
                                + "\"appointmentDate\":\"2026-10-05\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void step6_readUserProfileAfterFarmCreated() throws Exception {
        mockMvc.perform(get("/user/filter/1/profile"))
                .andExpect(status().isOk());
    }
}