package io.github.LuizYokoyama.SchedularPayments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.LuizYokoyama.SchedularPayments.controller.SchedularPaymentsController;
import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.repository.RecurrenceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.*;
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
public class SchedularPaymentsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SchedularPaymentsController schedularPaymentsController;

    @Autowired
    RecurrenceRepository recurrenceRepository;

    private final static CreateRecurrenceDto createRecurrenceDtoWithAccountNotExist;
    private final static CreateRecurrenceDto createRecurrenceDto;
    private final static String jsonCcreateRecurrenceDto;
    private final static String jsonCcreateRecurrenceDtoWithAccountNotExist;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("test_bk").withUsername("admin").withPassword("admin")
            .withInitScript("db_init.sql");

    static {
        postgreSQLContainer.start();
    }

    static {
        createRecurrenceDto = new CreateRecurrenceDto(1, "Test", LocalDate.now(),
                                                        2, 5.1f, 1);
        createRecurrenceDtoWithAccountNotExist = new CreateRecurrenceDto(5, "Test", LocalDate.now(),
                2, 5.1f, 1);


        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            jsonCcreateRecurrenceDto = mapper.writeValueAsString(createRecurrenceDto);
            jsonCcreateRecurrenceDtoWithAccountNotExist = mapper.writeValueAsString(createRecurrenceDtoWithAccountNotExist);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

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
    void testCreateRecurrenceFailed()  {

        Exception exception = assertThrows(RuntimeException.class, () -> schedularPaymentsController.createScheduledPayment(createRecurrenceDto) );
        System.out.println(exception.getMessage() + exception.getCause());
    }

    @Test
    @Order(value = 3)
    void testPostRecurrenceFailed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/recurrences")).andExpect(status().isBadRequest());

    }

    @Test
    @Order(value = 4)
    void testPostRecurrenceWithAccountNotFound() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.post("/v1/recurrences")
                .contentType(MediaType.APPLICATION_JSON).content(jsonCcreateRecurrenceDtoWithAccountNotExist)).andExpect(status().isNotFound());



    }
}
