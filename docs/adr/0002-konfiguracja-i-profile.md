# ADR-0002: Konfiguracja, profile środowiskowe i sekrety

Status: zaakceptowana · Data: 2026-09-25

## Kontekst
Aplikacja działa w kilku środowiskach (lokalnie, w testach, później na serwerze). Dane dostępowe nie mogą trafić do repozytorium jako konfiguracja produkcyjna.

## Decyzja
- `application.yml` zawiera tylko ustawienia wspólne i bezpieczne dla każdego środowiska. Nie ma w nim adresu bazy ani haseł.
- Profil `local` (`application-local.yml`) łączy się z bazą z `docker-compose.yml`. Wartości domyślne służą wyłącznie do pracy lokalnej i można je nadpisać zmiennymi `DB_*`.
- Profil `test` nie zawiera danych bazy: dostarcza je Testcontainers przez `@ServiceConnection`.
- Środowiska serwerowe dostaną własny profil i dane wyłącznie ze zmiennych środowiskowych, gdy będą potrzebne.
- `spring.jpa.hibernate.ddl-auto=validate`: Hibernate nigdy nie zmienia schematu bazy. Narzędzie do migracji (Flyway) dodamy razem z pierwszą tabelą.
- `spring.jpa.open-in-view=false`: brak ukrytych zapytań do bazy poza transakcją serwisu.
- Actuator wystawia publicznie tylko `health`. Szczegóły (np. stan bazy) są widoczne tylko w profilach `local` i `test`.

## Konsekwencje
- Aplikacja uruchomiona bez profilu nie połączy się z żadną bazą i nie wystartuje. To celowe: środowisko trzeba wskazać świadomie.
- `./mvnw spring-boot:run` ma w `pom.xml` ustawiony profil `local`, więc lokalny start nie wymaga dodatkowych flag.
