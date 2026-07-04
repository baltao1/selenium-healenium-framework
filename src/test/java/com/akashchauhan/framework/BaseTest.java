package com.akashchauhan.framework;

import com.epam.healenium.SelfHealingDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Boots a headless Chrome wrapped in a {@link SelfHealingDriver}. Every test
 * drives the self-healing driver, so a broken locator is transparently healed
 * against the healenium backend instead of throwing NoSuchElementException.
 */
public abstract class BaseTest {

    protected SelfHealingDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1280,900"
        );
        ChromeDriver delegate = new ChromeDriver(options);
        driver = SelfHealingDriver.create(delegate);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * The page under test is supplied per run via -DpageUrl. CI points the
     * "learn" run at the intact page and the "heal" run at the page whose
     * login-button id has changed.
     */
    protected String pageUrl() {
        String url = System.getProperty("pageUrl");
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Missing -DpageUrl system property");
        }
        return url;
    }
}
