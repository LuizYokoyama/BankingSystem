package io.github.LuizYokoyama.BankAccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.LuizYokoyama.BankAccount.controller.BankAccountController;
import io.github.LuizYokoyama.BankAccount.dto.CreateAccountDto;
import io.github.LuizYokoyama.BankAccount.dto.DepositDto;
import io.github.LuizYokoyama.BankAccount.dto.PeriodDto;
import io.github.LuizYokoyama.BankAccount.repository.AccountRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BankAccountController bankAccountController;

    @Autowired
    AccountRepository accountRepository;

    private final static CreateAccountDto CREATE_ACCOUNT_DTO;
    private final static CreateAccountDto CREATE_RECURRENCE_DTO_WITHOUT_NAME;
    private final static DepositDto DEPOSIT_DTO;
    private final static DepositDto DEPOSIT_DTO_WITHOUT_VALUE;
    private final static PeriodDto PERIOD_DTO;
    private final static PeriodDto PERIOD_DTO_WITHOUT_DATE;
    private final static String JSON_CREATE_ACCOUNT_DTO;
    private final static String JSON_CREATE_ACCOUNT_DTO_WITHOUT_NAME;
    private final static String JSON_DEPOSIT_DTO;
    private final static String JSON_DEPOSIT_DTO_WITHOUT_VALUE;
    private final static String JSON_PERIOD_DTO;
    private final static String JSON_PERIOD_DTO_WITHOUT_DATE;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("test_bk").withUsername("admin").withPassword("admin")
            .withInitScript("db_init.sql");

    static {
        postgreSQLContainer.start();
    }

    static {
        CREATE_ACCOUNT_DTO = new CreateAccountDto("Name Test");
        CREATE_RECURRENCE_DTO_WITHOUT_NAME = new CreateAccountDto("");
        DEPOSIT_DTO = new DepositDto(14f);
        DEPOSIT_DTO_WITHOUT_VALUE = new DepositDto(0);
        PERIOD_DTO = new PeriodDto(LocalDate.now(), LocalDate.now());
        PERIOD_DTO_WITHOUT_DATE = new PeriodDto(null, LocalDate.now());

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            JSON_CREATE_ACCOUNT_DTO = mapper.writeValueAsString(CREATE_ACCOUNT_DTO);
            JSON_CREATE_ACCOUNT_DTO_WITHOUT_NAME = mapper.writeValueAsString(CREATE_RECURRENCE_DTO_WITHOUT_NAME);
            JSON_DEPOSIT_DTO = mapper.writeValueAsString(DEPOSIT_DTO);
            JSON_DEPOSIT_DTO_WITHOUT_VALUE = mapper.writeValueAsString(DEPOSIT_DTO_WITHOUT_VALUE);
            JSON_PERIOD_DTO = mapper.writeValueAsString(PERIOD_DTO);
            JSON_PERIOD_DTO_WITHOUT_DATE = mapper.writeValueAsString(PERIOD_DTO_WITHOUT_DATE);
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
        Assertions.assertNotNull(bankAccountController);
        Assertions.assertNotNull(accountRepository);
    }

    @Test
    @Order(value = 2)
    void testMethodCreateAccountFailed()  {
        assertThrows(ConstraintViolationException.class, () -> bankAccountController.createAccount(CREATE_RECURRENCE_DTO_WITHOUT_NAME));

    }

    @Test
    @Order(value = 3)
    void testPostAccountWithWithoutNameFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_CREATE_ACCOUNT_DTO_WITHOUT_NAME)).andExpect(status().isBadRequest());
    }

    @Test
    @Order(value = 4)
    @Transactional
    void testPostAccountOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_CREATE_ACCOUNT_DTO)).andExpect(status().isCreated());
    }

    @Test
    @Order(value = 5)
    void testPutAccountWithoutValueFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_DEPOSIT_DTO_WITHOUT_VALUE)).andExpect(status().isBadRequest());
    }

    @Test
    @Order(value = 6)
    void testPutAccountDepositOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_DEPOSIT_DTO)).andExpect(status().isOk());
    }

    @Test
    @Order(value = 7)
    void testPutAccountStatementFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_PERIOD_DTO_WITHOUT_DATE)).andExpect(status().isBadRequest());
    }

    @Test
    @Order(value = 8)
    void testPutAccountStatementOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/statements/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_PERIOD_DTO)).andExpect(status().isOk());
    }

}
