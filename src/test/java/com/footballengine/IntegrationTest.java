package com.footballengine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Wspólna konfiguracja testów integracyjnych: pełny kontekst Springa,
 * profil "test", PostgreSQL z Testcontainers i MockMvc do testów HTTP.
 *
 * <p>Wszystkie testy z tą adnotacją współdzielą jeden kontekst Springa i jeden kontener bazy,
 * dlatego nie dokładamy do klas testowych adnotacji zmieniających kontekst.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public @interface IntegrationTest {
}
