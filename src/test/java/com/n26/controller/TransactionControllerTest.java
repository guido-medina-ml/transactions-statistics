package com.n26.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.Application;
import com.n26.domain.Statistics;
import com.n26.domain.Transaction;
import com.n26.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class TransactionControllerTest {
    @Autowired
    TransactionService transactionService;

    @Autowired
    ObjectMapper objectMapper;

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        transactionService.deleteTransactions();
    }

    @Test
    public void testAddTransactions() throws Exception {
        Transaction transaction = new Transaction(BigDecimal.valueOf(1000), new Timestamp(System.currentTimeMillis()));
        mockMvc.perform(post("/transactions")
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddOldTransaction() throws Exception {
        Transaction transaction = new Transaction(BigDecimal.valueOf(1000), new Timestamp(1L));
        mockMvc.perform(post("/transactions")
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetEmptyStatistics() throws Exception {
        Statistics statistics = new Statistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0L);
        mockMvc.perform(get("/statistics"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(objectMapper.writeValueAsString(statistics)));
    }

    @Test
    public void testGetStatisticsAddedTransaction() throws Exception {
        Transaction transaction = new Transaction(BigDecimal.valueOf(1000), new Timestamp(System.currentTimeMillis()));
        Statistics statistics = new Statistics(transaction);

        mockMvc.perform(post("/transactions")
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/statistics"))
                .andExpect(content().string(objectMapper.writeValueAsString(statistics)));
    }
}