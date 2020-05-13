package com.example.springjpa.repository;

import com.example.springjpa.domain.Customer;
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
        assertNotNull(this.customerRepository);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByCustomerId() {

        customerRepository.save(Customer.builder().customerId(999l).firstName("Nimit").lastName("Arora").build());
        customerRepository.save(Customer.builder().customerId(998l).firstName("Sonal").lastName("Arora").build());
        customerRepository.save(Customer.builder().customerId(997l).firstName("Divik").lastName("Arora").build());

        Optional<Customer> customer = this.customerRepository.findByCustomerId(998l);
        assertNotNull(customer.get());
        assertEquals("Sonal",  customer.get().getFirstName());
        assertNotNull(customer.get().getId());
    }

    @Test
    @Disabled("This is a broken test and needs fix")
    @Sql("createUser.sql")
    void findByCustomerId_withCustomSql() {
        Optional<Customer> customer = this.customerRepository.findByCustomerId(998l);
        assertNotNull(customer.get());
        assertEquals("Kapil",  customer.get().getFirstName());
        assertEquals(1, customer.get().getId());
    }

}