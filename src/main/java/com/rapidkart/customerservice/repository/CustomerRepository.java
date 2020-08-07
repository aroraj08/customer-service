package com.rapidkart.customerservice.repository;

import com.rapidkart.customerservice.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    //@Query(nativeQuery= true, value = "SELECT * FROM CUSTOMER c where  c.customer_id = :customer_id")
    Optional<Customer> findByCustomerId(@Param("customer_id") Long customerId);

    void deleteByCustomerId(Long customerId);
}
