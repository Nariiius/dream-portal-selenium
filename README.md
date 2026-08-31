# Dream Portal — Automated UI Testing (Selenium + JUnit 5)

Automated functional tests for the **Dream Portal** dream-journaling site
(https://arjitnigam.github.io/myDreams/), using **Selenium WebDriver, Java,
JUnit 5 and Maven**, structured with the **Page Object Model**, reported via
**Allure**, with **AI-based validation** of dream classifications.

---

## Features

- **Home page** — loading-animation lifecycle (appears, disappears after ~3s),
  main content + "My Dreams" button visibility, and the button opening **both**
  `dreams-diary.html` and `dreams-total.html` in new tabs (verified as distinct pages).
- **Dream log** — exactly 10 entries, every column filled, dream types limited
  to "Good" / "Bad".
- **Summary page** — Good 6, Bad 4, Total 10, Dreams This Week 7, Recurring 2.
- **Recurring-dreams logic** — the recurring count is recomputed from the raw
  diary data and cross-checked against the summary page.
- **Page Object Model** — all locators and actions live in page classes; tests
  contain no raw selectors.
- **Allure reporting** — interactive HTML report; a screenshot is attached
  automatically when a test fails.
- **Structured logging** — SLF4J 2 + Log4j2 logging configured to output real-time
  execution logs to the console and to `target/logs/test.log`.
- **AI validation (bonus)** — each dream name is classified by an AI model and
  compared against the table. Uses the free `nvidia/nemotron-3.5-lightning:free`
  model through OpenRouter's OpenAI-compatible API (no vendor SDK — Java's
  built-in `HttpClient`).

---

## Tech stack

| Component | Version / Choice |
|---|---|
| Java | 21+ (compiled at `--release 21`) |
| Build tool | Maven 3.9+ |
| Selenium | 4.25.0 (Selenium Manager auto-downloads the matching chromedriver) |
| Test framework | JUnit 5 (Jupiter 5.10.2) |
| Reporting | Allure (allure-junit5 2.29.0 + Allure CLI) |
| Logging | SLF4J 2.0.13 + Log4j2 2.23.1 (console & file appenders) |
| JSON | org.json (for the AI response) |

---

## Prerequisites

- **Java 21+** — `java -version`
- **Maven 3.9+** — `mvn -version`
- **Google Chrome** (stable)
- **Allure CLI** (for reports) — `brew install allure` on macOS

---

## Setup

### 1. Get an OpenRouter API key (required only for the AI test)

1. Go to **https://openrouter.ai/settings/keys**
2. Create a new key (free tier; the default model costs $0).
3. Copy the key — it looks like `sk-or-v1-...`.

> The rest of the suite runs without this key. The AI test **skips** when no key
> is present, so the build stays green either way.

### 2. Configure the key in an env file

The repository includes a template: **`env.example`**

```bash
cp env.example .env
```

Then open `.env` and replace the placeholder with your real key:

```
OPENROUTER_API_KEY=replace-with-your-real-key
```

**Why a `.env` file?** The key is a secret and must never be committed. `.env`
is listed in `.gitignore`; `env.example` (the template) IS committed so anyone
cloning the repo knows what to set. The code reads the key via
`Env.get("OPENROUTER_API_KEY")` (`src/test/java/com/dreamportal/ai/Env.java`),
which checks the real environment first and falls back to the `.env` file.

---

## Running the tests

```bash
mvn test
```

| Command | What it does |
|---|---|
| `mvn test` | Run the full suite (opens a real Chrome window) |
| `allure serve allure-results` | Open the interactive HTML report in your browser |
| `allure generate allure-results --clean -o allure-report` | Generate a static report folder |

---

## Project structure

```
dream-portal-selenium/
├── pom.xml
├── env.example                  # copy to .env and fill in the key
└── src/test/resources/
    └── log4j2.xml               # Log4j2 configuration (console & file)
└── src/test/java/com/dreamportal/
    ├── pages/                   # Page Object Model
    │   ├── HomePage.java
    │   ├── DreamsDiaryPage.java
    │   └── DreamsTotalPage.java
    ├── ai/                      # AI classification (bonus)
    │   ├── Env.java             # .env loader
    │   └── AiClassifier.java    # OpenAI-compatible chat completions call
    └── tests/
        ├── HomePageTest.java
        ├── DreamsDiaryTest.java
        ├── DreamsTotalTest.java
        ├── RecurringDreamsTest.java
        ├── AiDreamClassificationTest.java
        └── ScreenshotOnFailure.java   # JUnit extension: screenshot on failure
```

---

## Test coverage

| Test | Requirement verified |
|---|---|
| `loadingAnimationAppearsThenDisappears` | loader visible → disappears after ~3s → content + button visible |
| `myDreamsButtonOpensTwoTabs` | "My Dreams" opens both dreams-diary and dreams-total as distinct tabs |
| `diaryHasValidEntries` | exactly 10 entries; 3 filled columns; types only Good/Bad |
| `summaryStatsAreCorrect` | Good 6, Bad 4, Total 10, Dreams This Week 7, Recurring 2 |
| `recurringDreamsLogicMatchesSummary` | validates "Flying over mountains" & "Lost in maze" recur (>1) in diary and matches summary count |
| `aiClassifiesEveryDreamSameAsTable` | AI classification of every dream name matches the table |

---

## AI validation — how it works

- All dream names are collected from the diary and sent to the model in ONE
  request; the model returns a JSON array of classifications in the same order,
  which is compared to the table. Duplicate names are sent only once.
- **Deterministic** — `temperature: 0` so the same input gives the same answer.
- **Provider-agnostic request shape** — the code uses the OpenAI-compatible
  `chat/completions` API, so it can point at any compatible provider by changing
  `BASE_URL` and `MODEL` in `AiClassifier.java`.
- **Graceful skip** — without a key, the test is reported as *skipped*, not failed.

---

## Logging

- Test and page lifecycle events are logged via **SLF4J / Log4j2**.
- Logs are printed to the console in real-time and also persisted to **`target/logs/test.log`**.

---

## Notes

- Tests run in a real Chrome window; a full run takes ~40–60s.
- Failure screenshots appear in the Allure report under the failed test's page.
- The free AI tier is queue-limited and slow (~35–90s per run), so the AI test
  is the slowest one in the suite.
