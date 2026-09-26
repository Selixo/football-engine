package com.footballengine.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.footballengine.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@IntegrationTest
class HealthEndpointIntegrationTest {

    @Autowired
    private MockMvcTester mvc;

    @Test
    void healthIsUpAndIncludesDatabase() {
        assertThat(mvc.get().uri("/actuator/health"))
                .hasStatusOk()
                .bodyJson()
                .satisfies(json -> {
                    json.assertThat().extractingPath("$.status").isEqualTo("UP");
                    json.assertThat().extractingPath("$.components.db.status").isEqualTo("UP");
                    json.assertThat().extractingPath("$.components.db.details.database").isEqualTo("PostgreSQL");
                });
    }

    @Test
    void livenessAndReadinessProbesAreUp() {
        assertThat(mvc.get().uri("/actuator/health/liveness")).hasStatusOk();
        assertThat(mvc.get().uri("/actuator/health/readiness")).hasStatusOk();
    }

    @Test
    void otherActuatorEndpointsAreNotExposed() {
        assertThat(mvc.get().uri("/actuator/env")).hasStatus4xxClientError();
        assertThat(mvc.get().uri("/actuator/beans")).hasStatus4xxClientError();
    }
}
