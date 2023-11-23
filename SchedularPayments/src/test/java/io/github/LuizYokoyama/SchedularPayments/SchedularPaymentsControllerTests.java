package io.github.LuizYokoyama.SchedularPayments;

import io.github.LuizYokoyama.SchedularPayments.controller.SchedularPaymentsController;
import io.github.LuizYokoyama.SchedularPayments.repository.RecurrenceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class SchedularPaymentsControllerTests {

    @Autowired
    SchedularPaymentsController schedularPaymentsController;

    @Autowired
    RecurrenceRepository recurrenceRepository;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("test_bk").withUsername("admin").withPassword("admin")
            .withInitScript("create_tables.sql");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    @Order(value = 1)
    void testConnection() {
        Assertions.assertNotNull(recurrenceRepository);
        Assertions.assertNotNull(schedularPaymentsController);
    }

}
