# Football Engine

Backend platformy dla trenerów piłkarskich: drużyny, zawodnicy, treningi, frekwencja, mecze, statystyki i rozwój zawodników.
Architektura: **modularny monolit** w Spring Boot.

> Stan projektu: **Faza 1 (szkielet)**. Działa aplikacja z połączeniem do PostgreSQL i endpointem health. Funkcji biznesowych jeszcze nie ma.

## Technologie

| Technologia | Wersja | Po co |
|---|---|---|
| Java | 21 (LTS) | język aplikacji |
| Spring Boot | 4.1.1 | szkielet aplikacji, REST, konfiguracja |
| Maven (Wrapper) | 3.9.11 | budowanie; `./mvnw` nie wymaga instalacji Mavena |
| PostgreSQL | 17 | baza danych |
| Spring Data JPA / Hibernate | z Spring Boot | dostęp do bazy |
| Spring Boot Actuator | z Spring Boot | endpoint health |
| JUnit 5, Testcontainers | z Spring Boot | testy na prawdziwym PostgreSQL |
| Docker, Docker Compose | – | lokalna baza i uruchomienie aplikacji w kontenerze |

## Wymagania

- JDK 21+
- Docker z Docker Compose (baza lokalna i testy integracyjne)

## Uruchomienie

### Wariant 1: baza w Dockerze, aplikacja lokalnie (codzienna praca)

```bash
docker compose up -d          # startuje PostgreSQL na localhost:5432
./mvnw spring-boot:run        # startuje aplikację z profilem "local" na localhost:8080
```

### Wariant 2: wszystko w Dockerze

```bash
docker compose --profile app up --build
```

### Sprawdzenie

```bash
curl http://localhost:8080/actuator/health
# {"status":"UP","components":{"db":{"status":"UP",...},...}}
curl http://localhost:8080/actuator/health/liveness
curl http://localhost:8080/actuator/health/readiness
```

### Zatrzymanie

```bash
docker compose --profile app down        # zatrzymuje kontenery, dane bazy zostają w wolumenie
docker compose --profile app down -v     # dodatkowo usuwa dane bazy
```

## Profile środowiskowe

| Profil | Plik | Do czego | Skąd baza |
|---|---|---|---|
| (wspólne) | `application.yml` | ustawienia dla każdego środowiska | – |
| `local` | `application-local.yml` | praca na komputerze dewelopera | `docker-compose.yml` |
| `test` | `src/test/resources/application-test.yml` | testy automatyczne | Testcontainers (kontener tworzony na czas testów) |

Dane dostępowe do bazy w profilu `local` można nadpisać zmiennymi środowiskowymi:
`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`. Te same zmienne czyta `docker-compose.yml` (także z pliku `.env`, który jest ignorowany przez git).

## Testy

```bash
./mvnw verify
```

Testy integracyjne startują własny PostgreSQL w kontenerze (Testcontainers), więc wymagają działającego Dockera, ale **nie** wymagają `docker compose up`.

| Test | Co sprawdza |
|---|---|
| `FootballEngineApplicationTests` | kontekst Springa startuje z prawdziwą bazą |
| `HealthEndpointIntegrationTest` | health = `UP`, w tym połączenie z PostgreSQL; działają sondy liveness/readiness; inne endpointy Actuatora nie są wystawione |

Nowy test integracyjny: oznacz klasę adnotacją `@IntegrationTest`. Wszystkie takie testy współdzielą jeden kontekst i jeden kontener bazy.

## Struktura projektu

```
src/main/java/com/footballengine/
├── FootballEngineApplication.java
├── identity/          konta trenerów, uwierzytelnianie
├── teams/             drużyny
├── players/           zawodnicy i kadra
├── training/          treningi i frekwencja
├── matches/           mecze i zdarzenia meczowe
├── statistics/        statystyki
├── analytics/         analiza danych
├── recommendations/   rekomendacje treningowe
├── aicoach/           AI Coach (LLM, RAG)
├── notifications/     powiadomienia
├── audit/             dziennik zmian
└── infrastructure/    elementy techniczne wspólne dla modułów
```

Każdy moduł to pakiet najwyższego poziomu. Opis odpowiedzialności i zasad jest w `package-info.java` modułu.
Decyzje architektoniczne są w [`docs/adr`](docs/adr).
