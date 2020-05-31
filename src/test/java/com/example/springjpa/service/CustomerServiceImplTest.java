package com.example.springjpa.service;

import com.example.springjpa.domain.Customer;
import com.example.springjpa.exceptions.CustomerNotFoundException;
import com.example.springjpa.mapper.AddressMapper;
import com.example.springjpa.mapper.AddressMapperImpl;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashSet;
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

    private final CustomerMapper customerMapper = new CustomerMapperImpl();
    private final AddressMapper addressMapper = new AddressMapperImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(customerService, "customerMapper", customerMapper);
        ReflectionTestUtils.setField(customerService, "addressMapper" , addressMapper);
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

        Optional<Customer> customerObj = Optional.of(Customer.builder().customerId(1L)
            .firstName("first").lastName("last").build());

        when(customerRepository.findByCustomerId(1L)).thenReturn(customerObj);

        CustomerDto customerDto = this.customerService.getCustomerById(1L);
        verify(this.customerRepository).findByCustomerId(any());

        assertNotNull(customerDto);
        assertEquals("first", customerDto.getFirstName());
    }

    @Test
    void getCustomerByIdWhenNotExists() throws CustomerNotFoundException {

        when(customerRepository.findByCustomerId(any())).thenReturn(Optional.ofNullable(null));
        assertThrows(CustomerNotFoundException.class, () -> this.customerService.getCustomerById(2L));
    }

    @Test
    void updateCustomer() throws CustomerNotFoundException {

        Optional<Customer> customerObj = Optional.of(Customer.builder().customerId(2L).firstName("first")
                                        .lastName("last").build());

        Customer customerToBeSaved = Customer.builder()
                        .customerId(2L)
                        .firstName("Second")
                        .lastName("last")
                        .addressSet(new HashSet<>())
                        .build();

        when(customerRepository.findByCustomerId(2L)).thenReturn(customerObj);
        when(customerRepository.save(any())).thenReturn(customerToBeSaved);

        CustomerDto customerDto = CustomerDto.builder().firstName("Second").lastName("last").build();
        CustomerDto returnedCustomer = this.customerService.updateCustomer(2L, customerDto);

        assertNotNull(returnedCustomer);
        assertEquals("Second", returnedCustomer.getFirstName());

    }

    @Test
    void updateCustomerWhenCustomerNotExists() {

        when(customerRepository.findByCustomerId(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(CustomerNotFoundException.class, () -> {
           this.customerService.updateCustomer(50L, CustomerDto.builder().build());
        });

    }

    @Test
    void saveCustomer() {

        Customer savedCustomer = Customer.builder().customerId(12L)
                .firstName("dummy").lastName("dummy")
                .build();

        when(this.customerRepository.save(any())).thenReturn(savedCustomer);

        Long customerId = this.customerService.saveCustomer(CustomerDto.builder().build());
        assertEquals(12L, customerId);
    }

    @Test
    void deleteCustomer() {
        doNothing().when(this.customerRepository).deleteByCustomerId(1L);

        this.customerService.deleteCustomer(1L);
        verify(this.customerRepository).deleteByCustomerId(1L);
    }

    public List<Customer> setUpCustomerData() {

        List<Customer> customerList = new ArrayList<>();
        customerList.add(Customer.builder()
                .firstName("test_first")
                .lastName("test_last")
                .customerId(1234L)
                .build()
        );

        customerList.add(Customer.builder()
                .firstName("test_first")
                .lastName("test_last2")
                .customerId(1235L)
                .build()
        );

        return customerList;
    }
}