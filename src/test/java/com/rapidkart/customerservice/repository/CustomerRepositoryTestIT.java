package com.rapidkart.customerservice.repository;

import com.rapidkart.customerservice.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTestIT {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void checkAutowiring() {
        assertNotNull(customerRepository);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    @Disabled
    void findByCustomerId() {

        customerRepository.save(Customer.builder().customerId(1001l).firstName("Nimit").lastName("Arora").build());
        customerRepository.save(Customer.builder().customerId(1002l).firstName("Sonal").lastName("Arora").build());
        customerRepository.save(Customer.builder().customerId(1003l).firstName("Divik").lastName("Arora").build());

        Optional<Customer> customer = customerRepository.findByCustomerId(1001l);
        assertNotNull(customer.get());
        assertEquals("Sonal",  customer.get().getFirstName());
        assertNotNull(customer.get().getCustomerId());
    }

    @Test
    @Disabled("This is a broken test and needs fix")
    @Sql("createUser.sql")
    void findByCustomerId_withCustomSql() {
        Optional<Customer> customer = customerRepository.findByCustomerId(998l);
        assertNotNull(customer.get());
        assertEquals("Kapil",  customer.get().getFirstName());
        assertEquals(1, customer.get().getCustomerId());
    }

}