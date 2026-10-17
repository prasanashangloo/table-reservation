// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.selenium;

import java.util.UUID;

/** Central test data for the Selenium journeys (Task 9: "test data"). */
final class TestData {
    private TestData() {}

    static final String USER_NAME = "Selenium User";
    static final String PASSWORD = "secret123";
    static final String WRONG_EMAIL = "nobody@test.com";
    static final String WRONG_PASSWORD = "wrong-pass";

    /** Unique e-mail per test so journeys never depend on each other. */
    static String uniqueEmail() {
        return "selenium-" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
    }
}
