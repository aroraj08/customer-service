package com.rapidkart.customerservice.controller;

import com.rapidkart.model.CustomerDto;
import com.rapidkart.customerservice.exceptions.CustomerNotFoundException;
import com.rapidkart.customerservice.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
@Validated // for enabling method valid validation
public class CustomerController {

    private final CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class.getName());

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("customerId") Long customerId)
            throws CustomerNotFoundException {

        CustomerDto customer = this.customerService.getCustomerById(customerId);
        return new ResponseEntity<CustomerDto>(customer, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {

        Optional<List<CustomerDto>> customerList =
                this.customerService.getCustomers();
        return ResponseEntity.ok(customerList.get());
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Long> addNewCustomer(@Valid @RequestBody CustomerDto customerDto) {

        Long customerId = this.customerService.saveCustomer(customerDto);
        return ResponseEntity
                .created(URI.create("/api/v1/customers/" + customerId.longValue()))
                .build();
    }

    @PutMapping("/{customerId}")
    public ResponseEntity updateCustomer(@NotNull @PathVariable("customerId") Long customerId,
                                         @Valid @RequestBody CustomerDto customerDto)
            throws CustomerNotFoundException {

        this.customerService.updateCustomer(customerId, customerDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable("customerId") Long customerId) {
        this.customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

}
