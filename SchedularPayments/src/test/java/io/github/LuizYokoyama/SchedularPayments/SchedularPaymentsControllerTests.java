package io.github.LuizYokoyama.SchedularPayments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.LuizYokoyama.SchedularPayments.controller.SchedularPaymentsController;
import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.EditRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.exceptions.BadRequestRuntimeException;
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

import java.math.BigDecimal;
import java.time.LocalDate;

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

    private final static CreateRecurrenceDto CREATE_RECURRENCE_DTO;
    private final static CreateRecurrenceDto CREATE_RECURRENCE_DTO_ACCOUNT_NON_EXIST;
    private final static CreateRecurrenceDto CREATE_RECURRENCE_DTO_TO_SAME_ACCOUNT;
    private final static EditRecurrenceDto EDIT_RECURRENCE_DTO;
    private final static String JSON_CREATE_RECURRENCE_DTO;
    private final static String JSON_CREATE_RECURRENCE_DTO_ACCOUNT_NON_EXIST;
    private final static String JSON_EDIT_RECURRENCE_DTO;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("test_bk").withUsername("admin").withPassword("admin")
            .withInitScript("db_init.sql");

    static {
        postgreSQLContainer.start();
    }

    static {
        CREATE_RECURRENCE_DTO = new CreateRecurrenceDto(1, "Test", LocalDate.now(),
                                                        2, BigDecimal.TEN, 2);
        CREATE_RECURRENCE_DTO_ACCOUNT_NON_EXIST = new CreateRecurrenceDto(5, "Test", LocalDate.now(),
                                                                            2, BigDecimal.TEN, 1);
        CREATE_RECURRENCE_DTO_TO_SAME_ACCOUNT = new CreateRecurrenceDto(1, "Test", LocalDate.now(),
                2, BigDecimal.TEN, 1);
        EDIT_RECURRENCE_DTO = new EditRecurrenceDto(LocalDate.now(), 3, BigDecimal.TEN );

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            JSON_CREATE_RECURRENCE_DTO = mapper.writeValueAsString(CREATE_RECURRENCE_DTO);
            JSON_CREATE_RECURRENCE_DTO_ACCOUNT_NON_EXIST = mapper.writeValueAsString(CREATE_RECURRENCE_DTO_ACCOUNT_NON_EXIST);
            JSON_EDIT_RECURRENCE_DTO = mapper.writeValueAsString(EDIT_RECURRENCE_DTO);
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
    void testMethodCreateRecurrenceSameAccountFailed()  {
        Exception exception = assertThrows(BadRequestRuntimeException.class, () -> schedularPaymentsController.createScheduledPayment(CREATE_RECURRENCE_DTO_TO_SAME_ACCOUNT));
        assertEquals(exception.getMessage(), "Forneça uma conta de destino diferente desta conta" );
    }

    @Test
    @Order(value = 3)
    void testPostRecurrenceWithAccountNotFoundFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/recurrences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_CREATE_RECURRENCE_DTO_ACCOUNT_NON_EXIST)).andExpect(status().isNotFound());
    }

    @Test
    @Order(value = 4)
    void testPostRecurrenceWithAccountOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/recurrences")
                .contentType(MediaType.APPLICATION_JSON).content(JSON_CREATE_RECURRENCE_DTO)).andExpect(status().isCreated());
    }

    @Test
    @Order(value = 5)
    void testPatchRecurrenceOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/recurrences/18404c10-7edc-4c21-b606-5193aab6a342")
                .contentType(MediaType.APPLICATION_JSON).content(JSON_EDIT_RECURRENCE_DTO)).andExpect(status().isOk());
    }

    @Test
    @Order(value = 6)
    void testPatchRecurrenceNotFoundFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/recurrences/18404c10-7edc-4c21-b606-5193aab6a355")
                .contentType(MediaType.APPLICATION_JSON).content(JSON_EDIT_RECURRENCE_DTO)).andExpect(status().isNotFound());
    }

    @Test
    @Order(value = 7)
    void testCancelRecurrenceOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/recurrences/18404c10-7edc-4c21-b606-5193aab6a342"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(value = 8)
    void testCancelRecurrenceAlreadCanceledFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/recurrences/18404c10-7edc-4c21-b606-5193aab6a342"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(value = 9)
    void testCancelRecurrenceNotFoundFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/recurrences/18404c10-7edc-4c21-b606-5193aab6a355"))
                .andExpect(status().isNotFound());
    }

}
