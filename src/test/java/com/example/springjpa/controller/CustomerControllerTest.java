package com.example.springjpa.controller;

import com.example.springjpa.exceptions.CustomerNotFoundException;
import com.example.springjpa.model.CustomerDto;
import com.example.springjpa.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private static final String MAPPING = "/customer/api/v1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        this.customerDto =  CustomerDto.builder()
                .customerId(1234l)
                .firstName("dummy")
                .lastName("dummy").build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCustomerById() throws Exception, CustomerNotFoundException {

       when(this.customerService.getCustomerById(any()))
                .thenReturn(customerDto);

        this.mockMvc.perform
                (get(URI.create(MAPPING + "/" + customerDto.getCustomerId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.FirstName", is(customerDto.getFirstName())));

    }

    @Test
    void getAllCustomers() throws Exception {

        List<CustomerDto> customerDtoList = new ArrayList<>();
        customerDtoList.add(this.customerDto);

        Optional<List<CustomerDto>> customerDtoListOp =
                Optional.of(customerDtoList);

        when(this.customerService.getCustomers())
                .thenReturn(customerDtoListOp);

        this.mockMvc.perform(get(URI.create(MAPPING + "/all")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].FirstName", is(customerDtoList.get(0).getFirstName())));
    }

    @Test
    void addNewCustomer() throws Exception {

        Long customerId = 123l;

        String requestBody = "{\"customerId\":1002,\"FirstName\":\"Jatin\",\"LastName\":\"Arora\"}";

        when(this.customerService.saveCustomer(any()))
                .thenReturn(customerId);

        this.mockMvc.perform(post(MAPPING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(header().string("Location", "/customer/api/v1/" + customerId))
                .andExpect(status().isCreated());

    }

    @Test
    void updateCustomer() throws Exception, CustomerNotFoundException {

        Long customerId = 123l;
        String requestBody = "{\"customerId\":1002,\"FirstName\":\"Jatin\",\"LastName\":\"Arora\"}";

        when(this.customerService.updateCustomer(any(), any()))
                .thenReturn(this.customerDto);

        this.mockMvc
                .perform(MockMvcRequestBuilders.put(MAPPING + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomer() throws Exception {

        doNothing().when(this.customerService).deleteCustomer(any());
        this.mockMvc.perform(delete(URI.create(MAPPING + "/123")))
                .andExpect(status().isNoContent());
    }
}