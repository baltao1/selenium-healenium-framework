package com.akashchauhan.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the login screen. The login button is deliberately located
 * by id — in the "broken" page that id changes, which is exactly the locator
 * healenium has to heal.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By loginButton = By.id("btn-login"); // healed when id changes
    private final By message = By.id("message");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public LoginPage open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.presenceOfElementLocated(username));
        return this;
    }

    public LoginPage loginAs(String user, String pass) {
        driver.findElement(username).clear();
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(pass);
        driver.findElement(loginButton).click();
        return this;
    }

    public String messageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(message)).getText();
    }
}
