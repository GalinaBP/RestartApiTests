# RestartApiTests — API tests for Restart

Black-box API tests for the Restart backend: REST Assured 6 + TestNG 7 + Jackson 2.
Nothing here is copied from the backend repo; the contract is modelled from
`../doc restart/API_Test_Backlog.xlsx`.

## Running

Java 21 is required (the default `java` on this Mac is 26).

```bash
export JAVA_HOME=~/Library/Java/JavaVirtualMachines/corretto-21.0.7/Contents/Home
./mvnw test                                  # suite "api" against local
./mvnw test -Dsuite=smoke                    # seconds; safe on any environment
./mvnw test -Dsuite=local                    # everything that may run locally, slow tests last
./mvnw test -Dsuite=known-bugs               # only the tests pinned to open bugs
./mvnw test -Dsuite=smoke -Denv=railway      # needs SMOKE_PASSWORD in the environment
```

`-Dsuite=x` picks `src/test/resources/suites/testng-x.xml`; `-Denv=x` picks
`src/test/resources/config-x.properties` (default `local`).

The local target is the Docker stack, started from `../Restart`:

```bash
docker compose up --build --wait
```

In IntelliJ: run any class or method directly. For Railway put `-Denv=railway` in *VM options* and
`SMOKE_PASSWORD=…` in *Environment variables* of the run configuration.

## Layout

```
src/test/java
├── config/ConfigReader          which environment, its URL and smoke account
├── base/BaseTest                every test class extends it: clients + listeners
├── base/Groups                  group names as constants
├── api/                         framework: one client per area, the shared spec, two filters
│   ├── BaseApi                  apiSpec() → base URI + /api/v1 + JSON; rootSpec() → for /actuator
│   ├── AuthApiClient            register, login, refresh, forgotPassword, resetPassword, me…
│   ├── EligibilityApiClient     check
│   ├── HealthApiClient          health, liveness, readiness
│   ├── CorrelationIdFilter      sends X-Correlation-Id: <testMethod>-<8 hex> on every request
│   └── HttpRecorder             remembers this thread's requests/responses for the failure report
├── api/<area>/                  YOUR TESTS go here: api/smoke, api/auth, api/eligibility …
├── listeners/                   FailureReportListener, LocalOnlyGuard
├── models/dto/                  request and response records
└── utils/                       TestData, Passwords, EligibilityProfile, AuthSteps, JwtDecoder,
                                 ApiErrorAssert, Messages, ErrorCodes
```

## Writing a test — what you have

| You need | Use |
|---|---|
| Call an endpoint | `auth.register(email, password)`, `auth.login(...)`, `auth.me(token)`, `eligibility.check(body)`, `health.health()` — fields of `BaseTest`. Each returns the raw `Response`; the assertions are yours. |
| Read a body | `response.as(TokenResponse.class)`, `MeResponse`, `EligibilityCheckResponse`, `ApiError` |
| A unique email | `TestData.uniqueEmail()`; `TestData.uniqueId()` when you build the address yourself |
| A signed-in user | `AuthSteps.registerNewUser()` (local only) or `AuthSteps.loginSmokeUser()` (any env) |
| Password edge values | `Passwords.TOO_SHORT_7`, `MIN_8`, `MAX_72`, `TOO_LONG_73`, `SPACES_ONLY`, `EMPTY`, `PADDED`, `CYRILLIC_37_LETTERS_74_BYTES`, … and `Passwords.utf8Bytes(s)` |
| An eligibility body | `EligibilityProfile.baseline()` → `.with("maritalStatus", "MARRIED")`, `.without("debtRange")`, `.withExtra("role", "ADMIN")` → `.toRequest()` (typed) or `.toMap()` (anything) |
| Check an error | `ApiErrorAssert.assertApiError(response, 400, ErrorCodes.VALIDATION_ERROR, Messages.VALIDATION_FAILED)` returns the `ApiError`; then `assertFieldErrors(error, "password", Messages.PASSWORD_LENGTH)` |
| Token claims | `JwtDecoder.decode(token)` → `alg()`, `sub()`, `tokenUse()`, `email()`, `role()`, `iat()`, `exp()` |
| An `Authorization` header you control | `auth.meWithAuthorization("Bearer " + token)`, `auth.meWithoutToken()`, `AuthApiClient.bearer(token)` |
| A request no client makes (wrong method, `text/plain`, your own correlation id) | `given().spec(BaseApi.apiSpec()).contentType("text/plain").body("…").post(AuthApiClient.LOGIN)` |

`Messages` and `ErrorCodes` hold the exact server strings, checked against the backend source on
2026-11-15 — « », — and ё included.

## Decisions (and why — interview material)

**Broken bodies are `Map`s.** Positive tests send the records. Negative tests send a
`Map<String, Object>`: leave a key out for *missing*, `put(key, null)` for *explicit null*, any
value for *wrong type* (`"hasForeignAssets": "yes"`), extra keys for *mass assignment*. One way,
able to express everything a JSON body can, and it still goes through the same client.
`EligibilityProfile.with()` refuses an unknown field name, because the server ignores unknown
fields — a typo would turn a negative test into a silent positive one.

**Known bugs: tag, don't delete, don't leave red.** A test that fails because of a bug in
«Bugs found» gets `groups = {Groups.KNOWN_BUG, …}` and the bug id in `description`. The default
suites exclude it, so a red default run always means something *new* broke. `-Dsuite=known-bugs`
runs only them: when one turns green, the bug is fixed — remove the tag in the same change.

**Local-only is enforced, not remembered.** There is no delete-account endpoint, so anything that
registers a user must never run on Railway. Put `Groups.LOCAL_ONLY` on such tests — **on the class**
when its `@BeforeMethod` writes data. `LocalOnlyGuard` skips them (setup included) whenever
`local.only.allowed=false`, even when you run the class straight from IntelliJ, and
`AuthSteps.registerNewUser()` refuses on its own as a second lock.

**Parallel by method, 4 threads** in `api` and `local`. Safe because no test shares data with
another: every test makes its own user with a unique email. Timing and concurrency tests go in
`Groups.SLOW`; the `local` suite runs those afterwards, one at a time.

**Failures explain themselves.** Every request carries `X-Correlation-Id: <testMethod>-<random>`.
When a test fails, `FailureReportListener` prints every HTTP call that test made, with passwords and
tokens masked. For a 500: `docker logs restart-backend-1 | grep <that id>`.

**`registration-closed`** (API-017) needs a server started with registration switched off, so it
runs only in `-Dsuite=registration-closed`.

## Pitfalls worth knowing before you write

- In suites that *include* a group (`smoke`, `known-bugs`), `@BeforeMethod` runs only with
  `alwaysRun = true`. Without it the setup is silently skipped and the test hits a null.
- `fieldErrors` order is not stable — `assertFieldErrors` compares sorted lists, never indexes.
- `fieldErrors` is *absent* (not `[]`) on non-validation errors: `assertNoFieldErrors`.
- Two tokens minted in the same second are identical — assert a refreshed token *works*, not that it
  differs (API-024).
- Groups are strings: use `Groups.SMOKE`, not `"smoke"`, so a typo is a compile error.

## Which ticket uses what

| Ticket | Framework pieces |
|---|---|
| API-009 smoke | `health`, `EligibilityProfile.baseline().toRequest()`, `AuthSteps.loginSmokeUser()`; groups `SMOKE` |
| API-010, 011, 015, 019 | `auth.register`, `auth.me`, `TestData`, `MeResponse`; `LOCAL_ONLY` |
| API-012, 013, 021, 025 | `ApiErrorAssert`, `Messages`, `Passwords`, `Map` bodies for missing/null |
| API-014, 016 | `Passwords.CYRILLIC_*`, `KNOWN_BUG` (+ `SLOW` for 016) |
| API-017 | `REGISTRATION_CLOSED` |
| API-018 | `TestData.uniqueId()` inside your own addresses; `LOCAL_ONLY` |
| API-020 | `ApiErrorAssert`; compare whole `ApiError`s minus `timestamp` and `correlationId` |
| API-022, 023 | `SLOW` + `LOCAL_ONLY` |
| API-024 | `AuthSteps.registerNewUser()`, `auth.refresh`, `JwtDecoder` |

## Cleaning the local database

Every account a test creates contains `qa-` (that is what `TestData.uniqueId()` is for — use it in
any address you build). To delete them, and their reset tokens and payments:

```bash
./scripts/cleanup-local.sh
```

The demo account stays. Railway is never touched by this script.
