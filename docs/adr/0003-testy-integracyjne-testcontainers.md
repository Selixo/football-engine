# ADR-0003: Testy integracyjne na prawdziwym PostgreSQL (Testcontainers)

Status: zaakceptowana · Data: 2026-09-25

## Kontekst
Baza w pamięci (np. H2) różni się od PostgreSQL typami, funkcjami i zachowaniem SQL. Testy przechodzące na H2 mogą nie działać na produkcji.

## Decyzja
- Testy integracyjne używają PostgreSQL w kontenerze (Testcontainers) w tej samej wersji co `docker-compose.yml` (`postgres:17-alpine`).
- Konfiguracja jest w jednym miejscu: `TestcontainersConfiguration` + adnotacja `@IntegrationTest` (pełny kontekst, profil `test`, MockMvc).
- Wszystkie testy integracyjne współdzielą jeden kontekst Springa i jeden kontener, żeby testy były szybkie.

## Konsekwencje
- Uruchomienie testów wymaga Dockera (lokalnie i w CI).
- Testy sprawdzają rzeczywiste zachowanie bazy, której używamy.
- Klasy testowe nie powinny dokładać adnotacji zmieniających kontekst (np. `@MockitoBean`), bo każda taka zmiana tworzy nowy kontekst i nowy kontener.
