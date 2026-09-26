# ADR-0001: Modularny monolit z modułami jako pakietami najwyższego poziomu

Status: zaakceptowana · Data: 2026-09-25

## Kontekst
System obejmuje wiele obszarów domeny (Identity, Teams, Players, Training, Matches, Statistics, Analytics, Recommendations, AI Coach, Notifications, Audit). Zespół jest mały, a nie ma potrzeby niezależnego skalowania ani wdrażania części systemu.

## Decyzja
- Jedna aplikacja Spring Boot, jeden artefakt, jedna baza danych.
- Każdy moduł to pakiet najwyższego poziomu w `com.footballengine` (np. `com.footballengine.teams`). Dzielimy kod według obszaru domeny, a nie według warstw (`controller`, `service`...).
- Moduł udostępnia innym tylko publiczne API (podpakiet `api`). Implementacja (podpakiet `internal`) jest prywatna.
- Moduły odwołują się do danych innych modułów tylko przez identyfikatory, bez relacji JPA między modułami.
- `infrastructure` zawiera wyłącznie elementy techniczne i nie zależy od modułów domenowych.

## Konsekwencje
- Proste budowanie, uruchamianie, debugowanie i transakcje.
- Wyraźne granice pozwolą w przyszłości wydzielić moduł, jeśli pojawi się realna potrzeba.
- Granice w samym Javie nie są wymuszane przez kompilator. Automatyczną weryfikację granic (test architektury) dodamy, gdy powstanie pierwszy kod w więcej niż jednym module.
- Na razie pakiety modułów zawierają tylko `package-info.java` z opisem odpowiedzialności. Podpakiety `api` i `internal` powstaną wraz z pierwszym kodem modułu.
