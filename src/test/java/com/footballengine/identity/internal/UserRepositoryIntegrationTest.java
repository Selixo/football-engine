package com.footballengine.identity.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.footballengine.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@IntegrationTest
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository users;

    @BeforeEach
    void cleanUp() {
        users.deleteAll();
    }

    @Test
    void savesUserWithGeneratedIdAndTimestamps() {
        User saved = users.saveAndFlush(coach("coach@example.com"));

        User found = users.findById(saved.getId()).orElseThrow();
        assertThat(found.getEmail()).isEqualTo("coach@example.com");
        assertThat(found.getRole()).isEqualTo(Role.COACH);
        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(found.getUpdatedAt()).isNotNull();
    }

    @Test
    void rejectsDuplicateEmail() {
        users.saveAndFlush(coach("coach@example.com"));

        assertThatThrownBy(() -> users.saveAndFlush(coach("coach@example.com")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private static User coach(String email) {
        return new User(email, "{bcrypt}hash", "Jan", "Kowalski", Role.COACH);
    }
}
