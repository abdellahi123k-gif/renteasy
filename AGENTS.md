# RentEasy — AGENTS.md

## Commands

```powershell
.\mvnw.cmd compile
.\mvnw.cmd test "-Dspring.profiles.active=dev"      # skip MySQL, use H2
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"

# Single test:
.\mvnw.cmd test -Dtest=RenteasyApplicationTests "-Dspring.profiles.active=dev"
```

## Profiles

- **Default `prod`** (`application.properties`) — requires MySQL at `localhost:3306/renteasy`. Always pass `-Dspring.profiles.active=dev` locally.
- **`dev`** (`application-dev.properties`) — H2 in-memory, `ddl-auto=update`, `show-sql=true`. Use for local dev.
- **`prod`** (`application-prod.properties`) — MySQL, `ddl-auto=update` (no Flyway/Liquibase).
- **Test class has no `@ActiveProfiles`** — inherits `prod` (fails without MySQL). Always pass `-Dspring.profiles.active=dev` to tests.

## Architecture

- Spring Boot 3.5 + Java 17, single-module Maven. Entrypoint: `RenteasyApplication.java`.
- **Two controller packages**: `controller/` (REST API, `@RestController`) and `web/` (Thymeleaf page controllers, `@Controller`).
- **Dual auth**: web pages use formLogin + HTTP session; `/api/**` uses JWT Bearer via `JwtAuthenticationFilter`.
- CSRF disabled for `/api/**` but enabled for web — every POST form needs `_csrf`.
- `spring.jpa.open-in-view=false` — no lazy-loading outside `@Transactional`. Repositories use `@EntityGraph` for eager fetches.
- Static manual mappers in `mapper/` (no MapStruct). Add mapper methods for new DTOs.
- `validation/` package is empty — add `@Size`, `@Future` inline on DTOs.
- Dependencies: `jjwt 0.11.5`, `springdoc-openapi 2.5.0`, `thymeleaf-extras-springsecurity6`, Lombok.
- SpringDoc OpenAPI at `/swagger-ui.html`. H2 console at `/h2-console`.
- `service/impl/` has 8 implementations. Ownership/RBAC checks now enforced via `SecurityUtils` (see Gotchas).
- `SecurityUtils` (`security/SecurityUtils.java`) component — inject anywhere to get the current authenticated `User` entity.
- `@EnableJpaAuditing` on `RenteasyApplication` + `@EntityListeners(AuditingEntityListener.class)` + `@CreatedDate`/`@LastModifiedDate` on all entities.
- Domain-specific exceptions in `exception/`: `LogementNotAvailableException` (409), `ReservationConflictException` (409), `UnauthorizedActionException` (403).

## Frontend — Thymeleaf Templates

### Template structure
```
templates/
├── home.html                          # Standalone landing page (unauthenticated)
├── error/
│   ├── 403.html                       # Access denied
│   ├── 404.html                       # Not found
│   └── 500.html                       # Server error
├── layout/
│   ├── base.html                      # Main layout: header + content + footer
│   ├── header.html                    # Navbar with role-based menu + logout
│   └── footer.html                    # Footer with copyright
├── fragments/
│   ├── alerts.html                    # Success/error flash messages
│   ├── forms.html                     # Reusable form field fragments (csrf, input, textarea, select)
│   └── pagination.html                # Page navigation for paginated lists
├── pages/
│   ├── home.html                      # Hero + feature cards (fragment for authenticated layout)
│   ├── auth/
│   │   ├── login-standalone.html      # Login form (standalone, no layout)
│   │   └── register-standalone.html   # Register form (standalone, no layout)
│   ├── logement/
│   │   ├── list.html                  # Paginated grid + search/filter form
│   │   ├── detail.html                # Full detail with image placeholder + metrics
│   │   └── form.html                  # Create/edit form with validation
│   ├── annonce/
│   │   ├── list.html                  # Tabbed list (all/active) + cards
│   │   └── form.html                  # Create form with logement selector
│   ├── reservation/
│   │   ├── list.html                  # Table with status badges + date range
│   │   ├── detail.html                # Detail with confirm/cancel actions
│   │   └── form.html                  # Create form with date pickers
│   ├── user/
│   │   └── profile.html               # User profile card with quick links
│   └── dashboard/
│       ├── admin.html                 # 4 stat cards (users, logements, reservations, annonces)
│       ├── proprietaire.html          # 3 stat cards (logements, annonces, reservations)
│       └── locataire.html             # 2 stat cards (total/active reservations)
```

### Design system (custom CSS in `base.html`)
- Cards: `border: none`, hover lift (`translateY(-2px)`), shadow transition
- Navbar: bottom shadow, brand weight 700
- Badges: `font-weight: 500`, `padding: 0.4em 0.7em`
- Buttons: hover lift (`translateY(-1px)`)
- Table headers: uppercase, small, letter-spacing
- Dashboard cards: colored top border (4px) with matching icon
- Images: card image placeholder with gradient background
- Responsive: `display-4` scales on mobile

### Nav active state
Active nav link detection uses JavaScript (not Thymeleaf `#request` which is unavailable in Thymeleaf 4):
```html
<script>
document.querySelectorAll('.navbar-nav .nav-link').forEach(function(link) {
    if (link.getAttribute('href') === window.location.pathname) {
        link.classList.add('active');
    }
});
</script>
```

## Thymeleaf layout system

- All authenticated pages use `layout/base.html` which replaces `~{layout/header :: header}`, `~{__${viewContent}__}`, and `~{layout/footer :: footer}`.
- **Critcial**: `layout/base.html:16` uses Thymeleaf **preprocessing syntax** `~{__${viewContent}__}`. Without `__...__`, the `:: content` fragment selector is treated as a literal part of the template name.
- Controllers set `viewContent` to `"pages/{section}/{view} :: content"` and `pageTitle`.
- **`th:text` with multi-word literals** must use single quotes: `th:text="'Mes réservations'"`. Unquoted text with spaces (`th:text="Mes réservations"`) causes a Thymeleaf parse error.
- Auth pages use standalone templates (`login-standalone.html`, `register-standalone.html`) that bypass the layout system.

## REST API — Route notes

| Endpoint | Method | Auth | Notes |
|---|---|---|---|
| `/api/health` | GET | public | |
| `/api/auth/register`, `/api/auth/login` | POST | public | Returns `ApiResponse<AuthResponse>` |
| `/api/users` | POST | auth | Admin only (was public) — role checked in service |
| `/api/users/me` | GET | auth | Current user by email from JWT |
| `/api/logements` | GET/POST | auth | GET is **paginated** (`Pageable`) |
| `/api/logements/search` | GET | auth | Query: ville, type, minPrix, maxPrix, disponible |
| `/api/logements/{id}` | GET/PUT/DELETE | auth | |
| `/api/annonces` | GET/POST | auth | GET is **unpaginated** `List` |
| `/api/annonces/active` | GET | auth | Unpaginated `List` |
| `/api/annonces/{id}` | GET/PUT/DELETE | auth | |
| `/api/reservations` | GET/POST | auth | GET is unpaginated `List` |
| `/api/reservations/{id}` | GET/DELETE | auth | |
| `/api/reservations/{id}/confirm` | PUT | auth | |
| `/api/reservations/{id}/cancel` | PUT | auth | |
| `/api/admin/dashboard` | GET | `ADMIN` | |
| `/api/admin/users` | GET | `ADMIN` | Returns all users |
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
| `RuntimeException` | 400 | Catch-all — business errors map here |

Exception handler only covers **API** (`@RestController`/`@RestControllerAdvice`). Web page 500s fall through to Spring Boot's `/error` error templates (`error/403.html`, `error/404.html`, `error/500.html`).

## Seed data

`DataInitializer` (`CommandLineRunner`) creates 3 roles (`ADMIN`, `PROPRIETAIRE`, `LOCATAIRE`) + admin user (`admin@renteasy.com` / `admin123`).

## Gotchas

- **Role checks on web page controllers** (`web/` package) are configured in `SecurityConfig` for dashboard routes: `/dashboard/admin/**` requires `ADMIN`, `/dashboard/proprietaire/**` requires `PROPRIETAIRE`, `/dashboard/locataire/**` requires `LOCATAIRE`. Other web routes (`/logements`, `/annonces`, `/reservations`, `/users/profile`) require only authentication.
- **Ownership/RBAC now enforced** — Proprietaire can only update/delete own Logements/Annonces. Reservation confirm only by logement owner. Reservation cancel/delete by locataire or logement owner. `getAllReservations()` filters by role (ADMIN sees all, PROPRIETAIRE sees own logement reservations, LOCATAIRE sees own).
- **`/api/users` POST** requires ADMIN role (was any authenticated user).
- **`SecurityUtils.getCurrentUser()`** is the standard way to get the authenticated `User` entity. Inject `SecurityUtils` in any service instead of `SecurityContextHolder` directly.
- **Login form uses `name="email"`** (not `username`) — configured via `usernameParameter("email")` in SecurityConfig.
- **`CustomUserDetails.getUsername()` returns email**. `getAuthorities()` returns the role name as a bare string (not `ROLE_` prefixed).
- **`CustomUserDetailsService` must throw `UsernameNotFoundException`** (not `ResourceNotFoundException`) — Spring Security's `DaoAuthenticationProvider` recognizes `UsernameNotFoundException` as a failed login attempt and redirects to `/auth/login?error`. Any other exception is wrapped in `InternalAuthenticationServiceException` which breaks the login flow.
- **JWT filter's `shouldNotFilter()`** duplicates `permitAll()` URLs — keep in sync when adding public routes.
- **JWT secret** (`jwt.secret-key`) is now a strong Base64-encoded 256-bit key (was weak). No refresh/revocation — expires after 1h (`jwt.expiration-ms=3600000`).
- **`InvalidCredentialsException`** was dead code — deleted. The service throws Spring's `BadCredentialsException`.
- **Service layer throws domain-specific exceptions** (`LogementNotAvailableException` → 409, `ReservationConflictException` → 409, `UnauthorizedActionException` → 403) instead of generic `RuntimeException`.
- **JPA cascade/orphan-removal**: `Logement` → `Reservation` and `User` → `Reservation` now have `cascade = CascadeType.ALL, orphanRemoval = true`. Deleting a parent cascades to related reservations.
- **`Reservation.status` is an enum** (`ReservationStatus: EN_ATTENTE, CONFIRMEE, ANNULEE`). Repository methods taking status must accept `ReservationStatus`, not `String` or Spring Data query derivation will fail.
- **Thymeleaf literal rule**: `th:text="value with spaces"` must be `th:text="'value with spaces'"` (single-quoted). Accented single words (`Confirmée`) are fine unquoted. Expressions like `${i + 1}` are fine.
- **Static resources** (CSS/JS/images) are served from CDN (Bootstrap), not from local classpath. Only `/webjars/**` is local.
- **`#request`, `#session`, `#servletContext`, `#response` unavailable** in Thymeleaf 4+ — use JavaScript for active nav detection instead.

explique toujour en arabe
