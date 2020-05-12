package com.example.springjpa.service;

import com.example.springjpa.domain.Customer;
import com.example.springjpa.exceptions.CustomerNotFoundException;
import com.example.springjpa.mapper.CustomerMapper;
import com.example.springjpa.mapper.CustomerMapperImpl;
import com.example.springjpa.model.CustomerDto;
import com.example.springjpa.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    private CustomerMapper customerMapper = new CustomerMapperImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(customerService, "customerMapper", customerMapper);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCustomers() {

        List<Customer> customerList =  setUpCustomerData();
        when(customerRepository.findAll())
                .thenReturn(customerList);

        Optional<List<CustomerDto>> customerDtoList = this.customerService.getCustomers();

        Mockito.verify(this.customerRepository, times(1)).findAll();

        assertNotNull(customerDtoList.get());
        assertEquals(2, customerDtoList.get().size());
        assertEquals("test_first", customerDtoList.get().get(0).getFirstName());
    }

    @Test
    void getCustomerById() throws CustomerNotFoundException {

        Optional<Customer> customerObj = Optional.of(Customer.builder().customerId(1l)
            .firstName("first").lastName("last").build());

        when(customerRepository.findByCustomerId(1l)).thenReturn(customerObj);

        CustomerDto customerDto = this.customerService.getCustomerById(1l);
        verify(this.customerRepository).findByCustomerId(any());

        assertNotNull(customerDto);
        assertEquals("first", customerDto.getFirstName());
    }

    @Test
    void getCustomerByIdWhenNotExists() throws CustomerNotFoundException {

        when(customerRepository.findByCustomerId(any())).thenReturn(Optional.ofNullable(null));
        assertThrows(CustomerNotFoundException.class, () -> this.customerService.getCustomerById(2l));
    }

    @Test
    void updateCustomer() throws CustomerNotFoundException {

        Optional<Customer> customerObj = Optional.of(Customer.builder().customerId(2l).firstName("first")
                                        .lastName("last").build());

        Customer customerToBeSaved = Customer.builder().customerId(2l).firstName("Second").lastName("last").build();
        when(customerRepository.findByCustomerId(2l)).thenReturn(customerObj);

        when(customerRepository.save(customerToBeSaved)).thenReturn(customerToBeSaved);

        CustomerDto customerDto = CustomerDto.builder().customerId(2l).firstName("Second").lastName("last").build();
        CustomerDto returnedCustomer = this.customerService.updateCustomer(2l, customerDto);

        assertNotNull(returnedCustomer);
        assertEquals("Second", returnedCustomer.getFirstName());

    }

    @Test
    void updateCustomerWhenCustomerNotExists() {

        when(customerRepository.findByCustomerId(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(CustomerNotFoundException.class, () -> {
           this.customerService.updateCustomer(50l, CustomerDto.builder().build());
        });

    }

    @Test
    void saveCustomer() {

        Customer savedCustomer = Customer.builder().customerId(12l).firstName("dummy").lastName("dummy").build();
        savedCustomer.setId(999l);

        when(this.customerRepository.save(any())).thenReturn(savedCustomer);

        Long customerId = this.customerService.saveCustomer(CustomerDto.builder().build());
        assertEquals(12l, customerId);
    }

    @Test
    void deleteCustomer() {
        doNothing().when(this.customerRepository).deleteByCustomerId(1l);

        this.customerService.deleteCustomer(1l);
        verify(this.customerRepository).deleteByCustomerId(1l);
    }

    public List<Customer> setUpCustomerData() {

        List<Customer> customerList = new ArrayList<>();
        customerList.add(Customer.builder()
                .firstName("test_first")
                .lastName("test_last")
                .customerId(1234l)
                .build()
        );

        customerList.add(Customer.builder()
                .firstName("test_first")
                .lastName("test_last2")
                .customerId(1235l)
                .build()
        );

        return customerList;
    }
}