package io.github.LuizYokoyama.SchedularPayments;

import io.github.LuizYokoyama.SchedularPayments.controller.SchedularPaymentsController;
import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.RecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.repository.RecurrenceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class SchedularPaymentsControllerTests {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    @Order(value = 2)
    void testCreateRecurrence()  {

        Exception exception = assertThrows(RuntimeException.class, () -> schedularPaymentsController.createScheduledPayment(null) );
        System.out.println(exception.getMessage());

    }

    @Test
    @Order(value = 3)
    void testGetEmployeeByIdFailed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/recurrences")).andExpect(status().isBadRequest());
       // Assertions.assertEquals(employees.get(1).getName(), employeeRepository.findById(2).get().getName());
    }
}
