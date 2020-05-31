package com.example.springjpa.service;

import com.example.springjpa.domain.Address;
import com.example.springjpa.exceptions.CustomerNotFoundException;
import com.example.springjpa.mapper.AddressMapper;
import com.example.springjpa.mapper.CustomerMapper;
import com.example.springjpa.model.AddressDto;
import com.example.springjpa.model.CustomerDto;
import com.example.springjpa.domain.Customer;
import com.example.springjpa.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper,
                               AddressMapper addressMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.addressMapper = addressMapper;
    }

    @Override
    public Optional<List<CustomerDto>> getCustomers() {

        List<Customer> customerList = new ArrayList<>();
        this.customerRepository.findAll().forEach(customerList :: add);

        // convert Customer list to CustomerDto List using MapStruct
        List<CustomerDto>  customerDtoList = customerList.stream()
                    .map(customerMapper :: customerToCustomerDto)
                    .collect(Collectors.toList());

        // asyncMethodExecution(); this was just for testing purpose
        return Optional.of(customerDtoList);
    }

    @Override
    public CustomerDto getCustomerById(Long customerId) throws CustomerNotFoundException {

        Customer customer = checkIfPresent(customerId);
        // map customer object to CustomerDto object using MapStruct
        return this.customerMapper.customerToCustomerDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto)
            throws CustomerNotFoundException {

        Customer customer = checkIfPresent(customerId);

        // update Customer domain object with data from Dto and save it back to DB
        customer.setCustomerId(customerId);
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());

        Set<AddressDto> addressDtoSet = customerDto.getAddressSet();
        Set<Address> addresses = new HashSet<>();

        if (addressDtoSet != null) {
            addresses  = addressDtoSet.stream()
                                    .map(a -> {
                                       Address address = this.addressMapper.addressDtoToAddress(a);
                                       address.setCustomer(customer);
                                       return address;
                                    }).collect(Collectors.toSet());
        }
        customer.setAddressSet(addresses);

        Customer savedCustomer = this.customerRepository.save(customer);
        return this.customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    public Long saveCustomer(CustomerDto customerDto) {

        // create customer object using customer Dto object
        Customer customer = this.customerMapper.customerDtoToCustomer(customerDto);

        if (customer.getAddressSet() != null) {
            customer.getAddressSet()
                    .forEach(a -> a.setCustomer(customer));
        }

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
        return customerObj.orElseThrow(() -> new CustomerNotFoundException("Customer not found : " + customerId));
    }


    private void asyncMethodExecution() {

        CompletableFuture<Void> result = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            return "Result from async call";
        }).thenAccept((r) -> logger.info("logged : {}", r ));

        CompletableFuture.allOf();
    }
}
