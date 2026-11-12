# CLAUDE.md — Project Context for Claude Code

## What this project is

Java 17 + Maven automation framework with two concerns:
- **UI tests** against https://www.saucedemo.com using Playwright
- **API tests** against https://jsonplaceholder.typicode.com using RestAssured

## Key architectural decisions

**Page Object Model**: Every page is a class extending `BasePage`. Locators are `private static final` strings. No locators live in test classes.

**Single config source**: All settings flow through `ConfigManager` (singleton) → `AppConfig` (typed static accessors). Tests never read `System.getProperty` directly.

**Browser lifecycle**: `BaseTest.setUp()` creates a fresh `Playwright → Browser → BrowserContext → Page` before every test method. `tearDown()` closes all in reverse. This is intentional — test isolation over speed.

**Thread safety**: `parallel="classes"` in TestNG means each test class runs on exactly one thread with its own `BaseTest` instance and therefore its own browser. This is safe without `ThreadLocal`. If you ever switch to `parallel="methods"`, convert the `page`, `browser`, `context`, `playwright` fields in `BaseTest` to `ThreadLocal`.

**Login helper**: `BaseTest.loginAsStandardUser()` is the shared login path used by `InventoryTest` and `CheckoutTest`. Do not duplicate login steps in test classes — call this.

**`getPageTitle()`**: Defined once in `BasePage` using selector `.title`. SauceDemo uses this same element across all pages. Do not add `getPageTitle()` overrides to individual page classes.

## Cross-browser testing

`testng-crossbrowser.xml` runs UI tests against Chromium, Firefox, and WebKit simultaneously (`parallel="tests" thread-count="3"`). Each `<test>` block passes `<parameter name="browser" value="..."/>` which TestNG injects into `BaseTest.setUp(@Optional String browserParam)`.

- All existing suites remain backward-compatible — when no `browser` parameter is in the XML, `setUp` falls back to `AppConfig.getBrowser()` (reads `browser=` from config.properties)
- API tests are excluded from cross-browser (they use no browser)
- Requires all browsers pre-installed: `mvn exec:java ... -Dexec.args="install"`
- `BrowserFactory.createBrowser(playwright, browserType)` accepts the explicit browser name; `createBrowser(playwright)` delegates to config

## Test suites

| File | Purpose | Command |
|---|---|---|
| `testng.xml` | Default sequential | `mvn test` |
| `testng-crossbrowser.xml` | 3 browsers in parallel | `mvn test -DsuiteXmlFile=src/test/resources/testng-crossbrowser.xml` |
| `testng-parallel.xml` | Single browser, classes parallel | `mvn test -DsuiteXmlFile=src/test/resources/testng-parallel.xml` |
| `testng-shard-ui.xml` | GHA UI runner | used by gitactionpipeline.yml |
| `testng-shard-api.xml` | GHA API runner | used by gitactionpipeline.yml |

Switch suite at runtime: `-DsuiteXmlFile=src/test/resources/<file>`.

## Adding a new page

1. Create `src/main/java/com/automation/pages/NewPage.java` extending `BasePage`
2. Declare all locators as `private static final String`
3. Return `this` from fluent methods, return the next page from navigation methods
4. Register the page in the relevant TestNG suite XML if it needs new test classes

## Adding a new test class

1. Extend `BaseTest` for UI tests (browser lifecycle is automatic)
2. Do NOT extend `BaseTest` for API-only tests (see `UserApiTest`)
3. Annotate test methods with `groups = "ui"` or `groups = "api"` to control which suite picks them up
4. Add the class to all relevant testng XML suite files

## Config overrides

Any `config.properties` key can be overridden at the command line:
```bash
mvn test -Dheadless=true -Dbrowser=firefox
```

The `ConfigManager.get(key)` method checks `System.getProperty` first.

## Dependency management

- Versions live in `pom.xml` `<properties>` block — never hardcode versions in `<dependencies>`
- `<dependencyManagement>` forces `commons-lang3` to `3.17.0` to override rest-assured's older transitive version
- Do not add `aspectjweaver` as a javaagent — it conflicts with newer JDKs. Allure works without it for our annotation set.

## Allure browser labelling

`AllureBrowserListener` (in `com.automation.listeners`) uses TestNG's `ITestListener.onTestStart()` to tag each test after Allure has started the test case lifecycle. It must be registered in the testng XML **after** `AllureTestNg` so the test case exists before labels are written.

What it does per test:
- Adds `browser` label (shows in sidebar filter)
- Appends `[chromium]` / `[firefox]` / `[webkit]` to the test name
- Makes `historyId` unique per browser so the Trend graph tracks each browser separately

`AllureEnvironmentWriter` writes `environment.properties` to `target/allure-results/` once per JVM run (guarded by `AtomicBoolean`). It is called from `BaseTest.setUp()`. Shows OS, Java, Playwright version, and both base URLs in the Allure Overview → Environment panel.

Only register `AllureBrowserListener` in cross-browser suites where multiple browsers run. For single-browser suites it is not needed (browser is implicit from config).

## Reporting

```bash
mvn allure:serve          # generate + open in browser
mvn allure:report         # generate static HTML only
```

For trend history locally:
```bash
mvn test allure:report antrun:run@save-allure-history
```

History persists in `allure-history/` (gitignored). In GitHub Actions it is cached per branch automatically.

## Credentials

`config.properties` is gitignored — it is never committed. `config.properties.example` is the committed template (contains the public SauceDemo demo credentials as reference values).

- **Local**: copy `config.properties.example` → `config.properties` and fill in values
- **CI**: GitHub Secrets `UI_USERNAME` and `UI_PASSWORD` are passed as `-D` Maven system properties in `gitactionpipeline.yml`. `ConfigManager.get(key)` checks `System.getProperty` first, so they override the blank file values automatically.
  - GitHub does not allow dots in secret names, so `ui.username` → `UI_USERNAME` and `ui.password` → `UI_PASSWORD` (uppercase + underscores). The workflow maps them back: `-Dui.username=${{ secrets.UI_USERNAME }}`.

Never put credentials back into `config.properties.example` as real values — it is the only properties file that IS committed.

## What NOT to do

- Do not add `getPageTitle()` to individual page classes — it already exists in `BasePage`
- Do not add `ThreadLocal` unless switching to `parallel="methods"`
- Do not use `RestAssured.baseURI` in test classes — it is set once in `BaseApiClient`
- Do not commit `allure-history/`, `target/`, or `*.class` files — all are gitignored
- Do not add `@BeforeClass` browser setup to UI test classes — `BaseTest` handles the lifecycle
- Do not call `Allure.label("browser", ...)` in `@BeforeMethod` — Allure's test case hasn't started yet; use `AllureBrowserListener` instead
- Do not register `AllureBrowserListener` before `AllureTestNg` in testng XML — ordering matters
