package com.akashchauhan.tests;

import com.akashchauhan.framework.BaseTest;
import com.akashchauhan.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * The same two assertions run twice in CI: once on the intact page (healenium
 * records the button element) and once on the page whose button id has changed.
 * If the second run still passes, the By.id("btn-login") locator was healed.
 */
public class LoginTest extends BaseTest {

    @Test
    public void validCredentialsShowWelcome() {
        LoginPage page = new LoginPage(driver).open(pageUrl());
        page.loginAs("admin", "secret");
        Assert.assertEquals(page.messageText(), "Welcome, admin!");
    }

    @Test
    public void invalidCredentialsShowError() {
        LoginPage page = new LoginPage(driver).open(pageUrl());
        page.loginAs("admin", "wrong");
        Assert.assertEquals(page.messageText(), "Invalid credentials");
    }
}
