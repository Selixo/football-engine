/**
 * Moduł Audit: dziennik zmian i zdarzeń w systemie.
 *
 * <p>Zasady modułu (modularny monolit):
 * <ul>
 *   <li>inne moduły korzystają wyłącznie z publicznego API modułu (pakiet {@code api}),</li>
 *   <li>implementacja (pakiet {@code internal}) jest prywatna dla modułu,</li>
 *   <li>moduły odwołują się do danych innych modułów tylko przez identyfikatory.</li>
 * </ul>
 */
package com.footballengine.audit;
