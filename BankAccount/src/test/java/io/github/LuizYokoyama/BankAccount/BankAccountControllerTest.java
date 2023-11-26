package io.github.LuizYokoyama.BankAccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.LuizYokoyama.BankAccount.controller.BankAccountController;
import io.github.LuizYokoyama.BankAccount.dto.CreateAccountDto;
import io.github.LuizYokoyama.BankAccount.repository.AccountRepository;
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
    private final static String JSON_CREATE_ACCOUNT_DTO;
    private final static String JSON_CREATE_ACCOUNT_DTO_WITHOUT_NAME;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("test_bk").withUsername("admin").withPassword("admin")
            .withInitScript("db_init.sql");

    static {
        postgreSQLContainer.start();
    }

    static {
        CREATE_ACCOUNT_DTO = new CreateAccountDto("Name Test", 500.5f);
        CREATE_RECURRENCE_DTO_WITHOUT_NAME = new CreateAccountDto("", -100.55f);


        ObjectMapper mapper = new ObjectMapper();
        try {
            JSON_CREATE_ACCOUNT_DTO = mapper.writeValueAsString(CREATE_ACCOUNT_DTO);
            JSON_CREATE_ACCOUNT_DTO_WITHOUT_NAME = mapper.writeValueAsString(CREATE_RECURRENCE_DTO_WITHOUT_NAME);
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
    @Order(value = 3)
    void testPostAccountWithAccountNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_CREATE_ACCOUNT_DTO_WITHOUT_NAME)).andExpect(status().isBadRequest());
    }
}
