package com.example.springjpa.service;

import com.example.springjpa.exceptions.CustomerNotFoundException;
import com.example.springjpa.model.CustomerDto;
import com.example.springjpa.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Optional<List<CustomerDto>> getCustomers();

    CustomerDto getCustomerById(Long customerId) throws CustomerNotFoundException;

    CustomerDto updateCustomer(Long customerId, CustomerDto c) throws CustomerNotFoundException;

    Long saveCustomer(CustomerDto c);

    void deleteCustomer(Long customerId);
}
