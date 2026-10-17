// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationApiTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    private String loginNewUser() throws Exception {
        String email = "u" + UUID.randomUUID() + "@test.com";
        mvc.perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test\",\"email\":\"" + email + "\",\"password\":\"secret1\"}"))
                .andExpect(status().isCreated());
        String body = mvc.perform(post("/api/users/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\",\"password\":\"secret1\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JsonNode node = mapper.readTree(body);
        return node.get("token").asText();
    }

    private String bookingJson(String date, String slot) {
        return "{\"tableId\":1,\"date\":\"" + date + "\",\"slot\":\"" + slot + "\",\"partySize\":2}";
    }

    @Test
    void healthEndpointReportsUp() throws Exception {
        mvc.perform(get("/api/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void registrationRejectsDuplicateEmail() throws Exception {
        String email = "dup" + UUID.randomUUID() + "@test.com";
        String json = "{\"name\":\"A\",\"email\":\"" + email + "\",\"password\":\"secret1\"}";
        mvc.perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void bookingRequiresLogin() throws Exception {
        mvc.perform(post("/api/bookings").contentType(MediaType.APPLICATION_JSON)
                .content(bookingJson(LocalDate.now().plusDays(3).toString(), "12:00")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void fullBookingLifecycleAndDoubleBookingIsRejected() throws Exception {
        String token = loginNewUser();
        String date = LocalDate.now().plusDays(10).toString();

        String created = mvc.perform(post("/api/bookings").header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON).content(bookingJson(date, "19:00")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(created).get("id").asLong();

        // same table + date + slot -> conflict
        mvc.perform(post("/api/bookings").header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON).content(bookingJson(date, "19:00")))
                .andExpect(status().isConflict());

        mvc.perform(post("/api/bookings/" + id + "/confirm").header("X-Auth-Token", token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CONFIRMED"));

        mvc.perform(post("/api/bookings/" + id + "/cancel").header("X-Auth-Token", token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CANCELLED"));

        // slot is free again after cancellation
        mvc.perform(post("/api/bookings").header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON).content(bookingJson(date, "19:00")))
                .andExpect(status().isCreated());
    }

    @Test
    void pastDatesAreRejected() throws Exception {
        String token = loginNewUser();
        mvc.perform(post("/api/bookings").header("X-Auth-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookingJson(LocalDate.now().minusDays(1).toString(), "12:00")))
                .andExpect(status().isBadRequest());
    }
}
