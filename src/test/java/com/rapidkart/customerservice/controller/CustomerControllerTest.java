package com.rapidkart.customerservice.controller;

import com.rapidkart.customerservice.domain.Customer;
import com.rapidkart.customerservice.exceptions.CustomerNotFoundException;
import com.rapidkart.customerservice.mapper.CustomerMapper;
import com.rapidkart.customerservice.mapper.CustomerMapperImpl;
import com.rapidkart.customerservice.model.CustomerDto;
import com.rapidkart.customerservice.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CustomerController.class})
class CustomerControllerTest {

    private static final String MAPPING = "/api/v1/customers";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    CustomerDto customerDto;

    CustomerMapper customerMapper = new CustomerMapperImpl();

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.customerDto =  CustomerDto.builder()
                .customerId(1234l)
                .firstName("dummy")
                .lastName("dummy").build();
    }

    @Test
    void getCustomerById() throws Exception, CustomerNotFoundException {

       when(this.customerService.getCustomerById(any()))
                .thenReturn(customerDto);

        Customer customer = this.customerMapper.customerDtoToCustomer(customerDto);
        assertEquals(customer.getFirstName(), customerDto.getFirstName());

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

        this.mockMvc.perform(get(URI.create(MAPPING)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].FirstName", is(customerDtoList.get(0).getFirstName())));
    }

    @Test
    void addNewCustomer() throws Exception {

        Long customerId = 123l;

        CustomerDto customerDto = CustomerDto.builder()
                                .firstName("Jatin")
                                .lastName("Arora")
                                .build();

        String requestBody = objectMapper.writeValueAsString(customerDto);

        when(this.customerService.saveCustomer(any()))
                .thenReturn(customerId);

        this.mockMvc.perform(post(MAPPING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(header().string("Location", "/api/v1/customers/" + customerId))
                .andExpect(status().isCreated());

    }

    @Test
    void updateCustomer() throws Exception, CustomerNotFoundException {

        Long customerId = 123l;

        CustomerDto customerDto = CustomerDto.builder()
                .firstName("Jatin")
                .lastName("Arora")
                .build();

        String requestBody = objectMapper.writeValueAsString(customerDto);

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