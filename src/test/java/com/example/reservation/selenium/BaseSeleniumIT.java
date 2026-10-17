// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.selenium;

import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Starts the full app on a random port and drives it with headless Chrome.
 * Selenium Manager (built into Selenium 4.6+) downloads a matching chromedriver automatically.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(ScreenshotOnFailure.class)
abstract class BaseSeleniumIT {

    @LocalServerPort
    int port;

    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        String bin = System.getProperty("chrome.bin", "");
        if (!bin.isBlank()) options.setBinary(bin);
        if (!"false".equalsIgnoreCase(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1280,900");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:" + port + "/");
    }

    @AfterEach
    void stopBrowser() {
        if (driver != null) driver.quit();
    }

    // ---------- shared test data + helpers ----------

    static String uniqueEmail() {
        return TestData.uniqueEmail();
    }

    WebElement visible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    void type(String id, String text) {
        WebElement el = visible(By.id(id));
        el.clear();
        el.sendKeys(text);
    }

    void click(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    void waitForMessage(String contains) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("msg"), contains));
    }

    void register(String name, String email, String password) {
        type("reg-name", name);
        type("reg-email", email);
        type("reg-password", password);
        click(By.id("reg-btn"));
        waitForMessage("Registration successful");
    }

    void login(String email, String password) {
        type("login-email", email);
        type("login-password", password);
        click(By.id("login-btn"));
        waitForMessage("Logged in");
    }

    void registerAndLogin() {
        String email = uniqueEmail();
        register(TestData.USER_NAME, email, TestData.PASSWORD);
        login(email, TestData.PASSWORD);
    }

    /** Checks availability and books the first free slot. */
    void bookFirstAvailableSlot() {
        click(By.id("check-btn"));
        click(By.cssSelector(".slot-btn:not([disabled])"));
        waitForMessage("requested (PENDING)");
    }

    String firstBookingStatus() {
        return visible(By.cssSelector("tr.booking-row td.status")).getText();
    }

    void waitForFirstBookingStatus(String expected) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("tr.booking-row td.status"), expected));
    }
}
