// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Demonstrates the failure-screenshot mechanism (Task 9). Skipped normally.
 * Run:  mvn verify -Dsimulate.failure=true
 * The assertion below fails on purpose and target/screenshots/deliberateFailureProducesScreenshot.png is written.
 */
class ScreenshotMechanismIT extends BaseSeleniumIT {

    @Test
    @DisplayName("Deliberate failure to prove a screenshot is captured")
    void deliberateFailureProducesScreenshot() {
        assumeTrue(Boolean.getBoolean("simulate.failure"), "set -Dsimulate.failure=true to run");
        assertEquals("This text is not on the page", visible(org.openqa.selenium.By.tagName("h1")).getText());
    }
}
