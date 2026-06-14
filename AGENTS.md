# RentEasy — AGENTS.md

## Commands

```powershell
.\mvnw.cmd compile
.\mvnw.cmd test "-Dspring.profiles.active=dev"
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
.\mvnw.cmd test -Dtest=RenteasyApplicationTests "-Dspring.profiles.active=dev"
```

## Profiles

- **Default `prod`** (`application.properties`) — requires MySQL at `localhost:3306/renteasy`. Always pass `-Dspring.profiles.active=dev` locally.
- **`dev`** (`application-dev.properties`) — H2 in-memory, `ddl-auto=update`, `show-sql=true`, multipart 10MB/20MB. Use for local dev.
- **`prod`** (`application-prod.properties`) — MySQL, `ddl-auto=update`.
- **Test class has no `@ActiveProfiles`** — inherits `prod` (fails without MySQL). Always pass `-Dspring.profiles.active=dev` to tests.

## Architecture

- Spring Boot 3.5 + Java 17, single-module Maven. Entrypoint: `RenteasyApplication.java`.
- **Two controller packages**: `controller/` (REST API, `@RestController`) and `web/` (Thymeleaf page controllers, `@Controller`).
- **Dual auth**: web pages use formLogin + HTTP session (`IF_REQUIRED`); `/api/**` uses JWT Bearer via `JwtAuthenticationFilter`.
- CSRF disabled for `/api/**` but enabled for web — every POST form needs `_csrf`.
- `spring.jpa.open-in-view=false` — no lazy-loading outside `@Transactional`. Repositories use `@EntityGraph` for eager fetches.
- Static manual mappers in `mapper/` (no MapStruct). Add mapper methods for new DTOs.
- `validation/` package is empty — add `@Size`, `@Future` inline on DTOs.
- `SecurityUtils` (`security/SecurityUtils.java`) — inject anywhere instead of `SecurityContextHolder` to get the current `User`.
- `@EnableJpaAuditing` on `RenteasyApplication` + `@EntityListeners(AuditingEntityListener.class)` on all entities.
- Domain exceptions: `LogementNotAvailableException` (409), `ReservationConflictException` (409), `UnauthorizedActionException` (403).
- `WebConfig`: maps `/uploads/**` to `file:uploads/`, configures i18n (French default, `?lang=` param).
- `WebControllerAdvice`: injects `currentUserFirstName`, `currentUserLastName`, `currentUserInitials` into all web page models.
- CORS wide open (all origins, methods, headers) via `SecurityConfig`.
- Lombok annotation processor explicitly configured in `maven-compiler-plugin`.

## i18n

- 3 bundles: `messages.properties` (English), `messages_fr.properties` (French), `messages_ar.properties` (Arabic).
- Default locale: `Locale.FRENCH`. Switch via `?lang=fr|en|ar`.
- Header template has language dropdown calling `switchLang()` JS function.
- Thymeleaf uses `#{}` syntax: `th:text="#{nav.logements}"`.

## File Uploads

- `FileStorageService` stores files to `uploads/` dir (created on demand), returns `/uploads/{uuid}_{original}`.
- Used by `LogementPageController` for image/video files on logements.
- Multipart config (10MB file / 20MB request) only in `application-dev.properties`.

## Frontend — Thymeleaf Templates

```
templates/
├── home.html
├── error/{403,404,500}.html
├── layout/{base,header,footer}.html
├── fragments/{alerts,forms,pagination}.html
├── pages/
│   ├── home.html
│   ├── auth/{login-standalone,register-standalone}.html
│   ├── logement/{list,detail,form,mine}.html
│   ├── annonce/{list,form}.html
│   ├── reservation/{list,detail,form}.html
│   ├── user/profile.html
│   └── dashboard/{admin,proprietaire,locataire}.html
```

### Thymeleaf layout system

- **Critical**: `layout/base.html:398` uses preprocessing syntax `~{__${viewContent}__}`. Without `__...__`, fragment selectors break.
- Controllers set `viewContent` to `"pages/{section}/{view} :: content"` and `pageTitle`.
- Standalone auth pages (`login-standalone.html`, `register-standalone.html`) bypass layout.
- `th:text="'Mes réservations'"` (single-quoted for multi-word). `th:text="Mes réservations"` fails.
- `#request`, `#session`, `#servletContext`, `#response` unavailable in Thymeleaf 4 — JS for active nav detection.
- Nav active state detected via JS in `header.html:46-54`.
- POST forms include CSRF: `~{fragments/forms :: csrf}` or `<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">`.
- Flash messages via `RedirectAttributes` keyed `success` / `error`, rendered by `fragments/alerts :: alerts`.

### Web page routes (non-API)

| Route | Method | Controllers |
|---|---|---|
| `/` | GET | `HomePageController` |
| `/auth/login`, `/auth/register` | GET/POST | `AuthPageController` |
| `/auth/logout` | POST | Security (logout) |
| `/logements` | GET/POST | `LogementPageController` — list/search + create |
| `/logements/mes-logements` | GET | Proprietaire's own logements |
| `/logements/{id}` | GET | Detail |
| `/logements/{id}/edit` | GET | Edit form |
| `/logements/{id}/delete` | POST | Delete |
| `/annonces` | GET/POST | `AnnoncePageController` |
| `/annonces/mes-annonces` | GET | Proprietaire's own annonces |
| `/reservations` | GET/POST | `ReservationPageController` |
| `/reservations/{id}` | GET | Detail |
| `/reservations/{id}/cancel` | POST | Cancel |
| `/users/profile` | GET | `UserPageController` |
| `/dashboard/admin` | GET | `ADMIN` only |
| `/dashboard/proprietaire` | GET | `PROPRIETAIRE` only |
| `/dashboard/locataire` | GET | `LOCATAIRE` only |

Dashboard routes have role guards in `SecurityConfig`. Other web routes require only authentication.

## REST API — Route notes

| Endpoint | Method | Auth | Notes |
|---|---|---|---|
| `/api/health` | GET | public | |
| `/api/auth/register`, `/api/auth/login` | POST | public | Returns `ApiResponse<AuthResponse>` |
| `/api/users` | POST | `ADMIN` | Role checked in service |
| `/api/users/me` | GET | auth | Current user by email from JWT |
| `/api/logements` | GET/POST | auth | GET is **paginated** (`Pageable`) |
| `/api/logements/search` | GET | auth | Paginated. Query: ville, type, minPrix, maxPrix, disponible |
| `/api/logements/{id}` | GET/PUT/DELETE | auth | |
| `/api/annonces` | GET/POST | auth | GET is unpaginated `List` |
| `/api/annonces/active` | GET | auth | Unpaginated `List` |
| `/api/annonces/{id}` | GET/PUT/DELETE | auth | |
| `/api/reservations` | GET/POST | auth | GET is unpaginated `List` |
| `/api/reservations/{id}` | GET/DELETE | auth | |
| `/api/reservations/{id}/confirm` | PUT | auth | |
| `/api/reservations/{id}/cancel` | PUT | auth | |
| `/api/admin/dashboard` | GET | `ADMIN` | |
| `/api/admin/users` | GET | `ADMIN` | |
| `/api/admin/users/{id}` | DELETE | `ADMIN` | Cannot delete own account |
| `/api/proprietaire/dashboard` | GET | `PROPRIETAIRE` | Uses `auth.getName()` for email |
| `/api/locataire/dashboard` | GET | `LOCATAIRE` | Uses `auth.getName()` for email |

All API responses use `ApiResponse<T>` envelope. DELETE returns 204 (no body). Errors use `ApiErrorResponse`.

## Error handling

`GlobalExceptionHandler` (`@RestControllerAdvice`):
| Exception | Status | Notes |
|---|---|---|
| `ResourceNotFoundException` | 404 | |
| `BadCredentialsException` | 401 | Message: "Invalid email or password" |
| `EmailAlreadyExistsException` | 409 | |
| `LogementNotAvailableException` | 409 | |
| `ReservationConflictException` | 409 | |
| `UnauthorizedActionException` | 403 | |
| `IllegalArgumentException` | 400 | |
| `MethodArgumentNotValidException` | 400 | Returns field errors map |
| `RuntimeException` | 400 | Catch-all |

Web page 500s fall through to Spring Boot's `/error` error templates.

## Seed data

`DataInitializer` (`CommandLineRunner`) creates 3 roles (`ADMIN`, `PROPRIETAIRE`, `LOCATAIRE`) + admin user (`admin@renteasy.com` / `admin123`). Also creates 2 proprios (Sophie Martin, Pierre Dupont), 3 locataires (Lucas Bernard, Emma Petit, Youssef Alami), 5 logements, 5 annonces, 4 reservations.

## Gotchas

- **`CustomUserDetails.getUsername()` returns email**. `getAuthorities()` returns role name as bare string (no `ROLE_` prefix).
- **Login form uses `name="email"`** (not `username`) — configured via `usernameParameter("email")` in `SecurityConfig`.
- **`CustomUserDetailsService` must throw `UsernameNotFoundException`** (not `ResourceNotFoundException`) — Spring Security's `DaoAuthenticationProvider` recognizes it as failed login. Other exceptions break the login flow.
- **`SecurityUtils.getCurrentUser()`** is the standard way to get the authenticated `User`. Inject instead of `SecurityContextHolder`.
- **Ownership/RBAC enforced**: Proprietaire can only update/delete own Logements/Annonces. Reservation confirm only by logement owner. Cancel/delete by locataire or owner. `getAllReservations()` filters by role.
- **`/api/users` POST** requires `ADMIN` role.
- **JWT filter's `shouldNotFilter()`** duplicates `permitAll()` URLs — keep in sync when adding public routes.
- **JWT secret** is Base64-encoded 256-bit key, expires after 1h (`jwt.expiration-ms=3600000`). No refresh/revocation.
- **`Reservation.status` is enum** (`ReservationStatus: EN_ATTENTE, CONFIRMEE, ANNULEE`). Repository methods must accept `ReservationStatus`, not `String`.
- **JPA cascade/orphan-removal**: `Logement` → `Reservation` and `User` → `Reservation` have `cascade = CascadeType.ALL, orphanRemoval = true`.
- **JWT `doFilterInternal`** catches `ResourceNotFoundException` specifically (for deleted users) and clears context.
- **Multipart config** is only in `application-dev.properties` — prod needs explicit config or file uploads will fail.
- **`dto/Dashboard/`** directory name uses capital D (watch imports).
- **`th:text` with multi-word literals** must use single quotes: `th:text="'Mes réservations'"`.
- **Static resources** served from CDN (Bootstrap, Bootstrap Icons, Google Fonts). Only `/webjars/**` is local.

explique toujour en arabe
