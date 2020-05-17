package com.example.springjpa.service;

import com.example.springjpa.exceptions.CustomerNotFoundException;
import com.example.springjpa.mapper.CustomerMapper;
import com.example.springjpa.model.CustomerDto;
import com.example.springjpa.domain.Customer;
import com.example.springjpa.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public Optional<List<CustomerDto>> getCustomers() {

        List<Customer> customerList = new ArrayList<>();
        this.customerRepository.findAll().forEach(c -> customerList.add(c));

        // convert Customer list to CustomerDto List using MapStruct
        List<CustomerDto>  customerDtoList = customerList.stream()
                    .map(c -> customerMapper.customerToCustomerDto(c))
                    .collect(Collectors.toList());

        return Optional.of(customerDtoList);
    }

    @Override
    public CustomerDto getCustomerById(Long customerId) throws CustomerNotFoundException {

        Customer customer = checkIfPresent(customerId);
        // map customer object to CustomerDto object using MapStruct
        CustomerDto customerDto = this.customerMapper.customerToCustomerDto(customer);
        return customerDto;
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, CustomerDto c) throws CustomerNotFoundException {

        Customer customer = checkIfPresent(customerId);
        // update Customer domain object with data from Dto and save it back to DB
        customer.setFirstName(c.getFirstName());
        customer.setLastName(c.getLastName());
        customer.setCustomerId(customerId);

        Customer savedCustomer = this.customerRepository.save(customer);
        return this.customerMapper.customerToCustomerDto(savedCustomer);

    }

    @Override
    public Long saveCustomer(CustomerDto customerDto) {

        // create customer object using customer Dto object
        Customer customer = this.customerMapper.customerDtoToCustomer(customerDto);

        // call repository to save customer
        Customer savedCustomer = this.customerRepository.save(customer);
        return savedCustomer.getCustomerId();
    }

    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {

        this.customerRepository.deleteByCustomerId(customerId);
    }

    private Customer checkIfPresent(Long customerId) throws CustomerNotFoundException {

        Optional<Customer> customerObj = customerRepository.findByCustomerId(customerId);
        Customer customer = customerObj.orElseThrow(() -> {
            return new CustomerNotFoundException("Customer not found : " + customerId);
        });

        return customer;
    }
}
