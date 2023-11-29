package io.github.LuizYokoyama.Payments;

import io.github.LuizYokoyama.Payments.controller.PaymentsController;
import io.github.LuizYokoyama.Payments.repository.RecurrenceRepository;
import org.hibernate.LazyInitializationException;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class PaymentsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PaymentsController paymentsController;

    @Autowired
    RecurrenceRepository recurrenceRepository;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("test_bk").withUsername("admin").withPassword("admin")
            .withInitScript("db_init.sql");

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
        Assertions.assertNotNull(paymentsController);
        Assertions.assertNotNull(recurrenceRepository);
    }

    @Test
    @Order(value = 2)
    void testMethodPaymentFailed()  {
        assertThrows(LazyInitializationException.class, () -> paymentsController.executeScheduledPayments());

    }

    @Test
    @Order(value = 3)
    void testPutPaymentsOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/payments")).andExpect(status().isOk());
    }
}


