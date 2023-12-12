package io.github.LuizYokoyama.Payments;

import io.github.LuizYokoyama.Payments.controller.PaymentsController;
import io.github.LuizYokoyama.Payments.repository.RecurrenceRepository;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.*;
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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    public static final String PATH_DB_INIT = "src/test/resources/__db_init.sql";
    public static final String PATH_DB_INIT_SWAP = "src/test/resources/db_init_swap.sql";
    public static final String DB_INIT = "__db_init.sql";
    public static final String DB_NAME = "test_bk";
    public static final String ADMIN = "admin";
    public static final String PASSWORD = "admin";
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName(DB_NAME).withUsername(ADMIN).withPassword(PASSWORD)
            .withInitScript(DB_INIT);

    static {
        //insere a data atual na inicialização do db
        FileWriter wr = null;
        try {
            String dbInit = new String(Files.readAllBytes(Paths.get(PATH_DB_INIT_SWAP)));
            dbInit = dbInit.replaceAll("data_atual", LocalDate.now().atTime(0,0).toString());
            wr = new FileWriter(PATH_DB_INIT);
            wr.write(dbInit);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
    void testPutPaymentsExpectOneExec() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/payments"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value("1")));

    }

    @Test
    @Order(value = 4)
    void testPutPaymentsExpectZeroExec() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/payments"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value("0")));
    }

    @Test
    @Order(value = 5)
    void testPutBalanceAggregationExpectTwoExec() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/aggregate"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value("2")));
    }

    @Test
    @Order(value = 5)
    void testPutBalanceAggregationExpectZeroExec() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/aggregate"))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$").value("0")));
    }

}


