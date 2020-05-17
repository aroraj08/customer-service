package com.example.springjpa.repository;

import com.example.springjpa.domain.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    //@Query(nativeQuery= true, value = "SELECT * FROM CUSTOMER c where  c.customer_id = :customer_id")
    Optional<Customer> findByCustomerId(@Param("customer_id") Long customerId);

    void deleteByCustomerId(Long customerId);
}
