// Author/Owner: Prasana Shangloo (23102A0076) | Copyright (c) 2026
package com.example.reservation.selenium;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/** Failure-screenshot mechanism (Task 9): saves target/screenshots/<test>.png when a test fails. */
public class ScreenshotOnFailure implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext ctx, Throwable t) throws Throwable {
        Object instance = ctx.getRequiredTestInstance();
        if (instance instanceof BaseSeleniumIT base && base.driver instanceof TakesScreenshot shooter) {
            try {
                File src = shooter.getScreenshotAs(OutputType.FILE);
                Path dir = Path.of("target", "screenshots");
                Files.createDirectories(dir);
                Files.copy(src.toPath(), dir.resolve(ctx.getRequiredTestMethod().getName() + ".png"),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ignored) {
                // never hide the real test failure
            }
        }
        throw t;
    }
}
