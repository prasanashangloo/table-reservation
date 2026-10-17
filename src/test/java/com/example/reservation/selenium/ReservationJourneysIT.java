// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** The five critical user journeys from docs/06-test-plan.md. */
class ReservationJourneysIT extends BaseSeleniumIT {

    @Test
    @DisplayName("J1: user can register and log in")
    void registerAndLoginJourney() {
        registerAndLogin();
        assertEquals(TestData.USER_NAME, visible(By.id("user-name")).getText());
    }

    @Test
    @DisplayName("J2: invalid login shows an error")
    void invalidLoginShowsError() {
        type("login-email", TestData.WRONG_EMAIL);
        type("login-password", TestData.WRONG_PASSWORD);
        click(By.id("login-btn"));
        waitForMessage("Invalid email or password");
        assertTrue(visible(By.id("msg")).getAttribute("class").contains("err"));
    }

    @Test
    @DisplayName("J3: availability view lists tables and slots")
    void availabilityJourney() {
        registerAndLogin();
        click(By.id("check-btn"));
        List<WebElement> rows = wait.until(d -> {
            List<WebElement> r = d.findElements(By.cssSelector(".table-row"));
            return r.isEmpty() ? null : r;
        });
        assertTrue(rows.size() >= 5, "party of 2 should fit at least 5 of the 6 tables");
        assertFalse(driver.findElements(By.cssSelector(".slot-btn")).isEmpty());
    }

    @Test
    @DisplayName("J4: booking request creates a PENDING booking, then can be confirmed")
    void bookAndConfirmJourney() {
        registerAndLogin();
        bookFirstAvailableSlot();
        assertEquals("PENDING", firstBookingStatus());

        click(By.cssSelector(".confirm-btn"));
        waitForFirstBookingStatus("CONFIRMED");
        assertEquals("CONFIRMED", firstBookingStatus());
    }

    @Test
    @DisplayName("J5: cancelling a booking updates its status and removes the action buttons")
    void cancelJourney() {
        registerAndLogin();
        bookFirstAvailableSlot();

        click(By.cssSelector(".cancel-btn"));
        waitForFirstBookingStatus("CANCELLED");
        assertEquals("CANCELLED", firstBookingStatus());
        assertTrue(driver.findElements(By.cssSelector("tr.booking-row .cancel-btn")).isEmpty());
    }
}
