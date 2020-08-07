package com.rapidkart.customerservice.service;

import com.rapidkart.customerservice.exceptions.CustomerNotFoundException;
import com.rapidkart.customerservice.model.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Optional<List<CustomerDto>> getCustomers();

    CustomerDto getCustomerById(Long customerId) throws CustomerNotFoundException;

    CustomerDto updateCustomer(Long customerId, CustomerDto c) throws CustomerNotFoundException;

    Long saveCustomer(CustomerDto c);

    void deleteCustomer(Long customerId);
}
