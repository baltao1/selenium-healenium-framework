# Selenium + Healenium Self-Healing Framework

[![CI](https://github.com/baltao1/selenium-healenium-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/baltao1/selenium-healenium-framework/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-17-orange)
![Selenium](https://img.shields.io/badge/Selenium-4-43b02a)
![Healenium](https://img.shields.io/badge/Healenium-3.5-6d4aff)
![License](https://img.shields.io/badge/license-MIT-blue)

A UI automation framework built with **Java 17, Selenium 4, TestNG and the
Page Object Model**, wrapped in **[Healenium](https://healenium.io/)** for
**self-healing locators**. When a locator breaks because the UI changed,
Healenium recovers it automatically instead of failing the test.

## The self-healing proof (this is the interesting part)

Most "Healenium demos" just add the dependency and never actually break a
locator. This suite proves healing end-to-end in CI by running **the same tests
twice against two versions of the page**:

| Run | Page | Login button | What happens |
|-----|------|--------------|--------------|
| 1 — *learn* | `login_v1.html` | `id="btn-login"` ✅ | Locator works; Healenium records the element in its backend |
| 2 — *heal* | `login_v2.html` | `id="login-submit"` ❌ | `By.id("btn-login")` now fails → **Healenium heals it** and the tests still pass |

The only difference between the two pages is the button's `id`. The click
handler is bound by tag/class, so the *page* still works — only the Selenium
locator is broken, which is exactly the real-world scenario self-healing solves.
**If run 2 is green, healing genuinely happened.**

## What it demonstrates

- **Self-healing locators** — `SelfHealingDriver.create(delegate)` transparently
  recovers broken `By` locators against the Healenium backend + selector-imitator.
- **Page Object Model** — locators and actions live in
  [`LoginPage`](src/test/java/com/akashchauhan/pages/LoginPage.java); tests read
  as intent.
- **Dockerised backend** — the full Healenium stack (backend, selector-imitator,
  Postgres) is brought up with one `docker compose up`.
- **Headless, reproducible CI** — Chrome via Selenium Manager (no manual driver
  management), the whole flow gated in GitHub Actions.
- **Config as properties** — healing behaviour (`score-cap`, `recovery-tries`,
  backend URLs) in [`healenium.properties`](src/test/resources/healenium.properties).

## Stack

| Concern | Tool |
|---------|------|
| Language | Java 17 |
| Browser automation | Selenium 4 (Selenium Manager for drivers) |
| Self-healing | Healenium `healenium-web` 3.5.8 + backend 3.5.1 |
| Test runner | TestNG 7 |
| Build | Maven (surefire) |
| Backend infra | Docker Compose — Postgres 15, hlm-backend, selector-imitator |

## Run it locally

```bash
# 1. Start the Healenium backend
docker compose up -d

# 2. Learn run — locators intact, healenium records the elements
mvn -B clean test "-DpageUrl=file://$(pwd)/app/login_v1.html"

# 3. Heal run — button id changed; watch the broken locator get healed
mvn -B test "-DpageUrl=file://$(pwd)/app/login_v2.html"
```

Requires JDK 17, Maven, Docker, and Chrome.

## Layout

```
app/
  login_v1.html            baseline page (button id = btn-login)
  login_v2.html            broken page  (button id = login-submit)
  login.js                 shared behaviour, bound by class not id
src/test/java/com/akashchauhan/
  framework/BaseTest.java  SelfHealingDriver + headless Chrome lifecycle
  pages/LoginPage.java     Page Object
  tests/LoginTest.java     two assertions, run against both pages
src/test/resources/
  healenium.properties     healing config (backend URLs, score-cap)
  testng.xml               suite definition
docker-compose.yml         Healenium backend stack
db/sql/init.sql            creates the healenium Postgres schema
.github/workflows/ci.yml   compose up -> learn run -> heal run
```

## License

MIT
