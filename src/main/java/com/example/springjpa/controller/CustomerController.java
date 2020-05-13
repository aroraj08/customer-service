package com.example.springjpa.controller;

import com.example.springjpa.model.CustomerDto;
import com.example.springjpa.exceptions.CustomerNotFoundException;
import com.example.springjpa.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer/api/v1")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("customerId") Long customerId)
            throws CustomerNotFoundException {

        CustomerDto customer = this.customerService.getCustomerById(customerId);
        return new ResponseEntity<CustomerDto>(customer, HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        Optional<List<CustomerDto>> customerList =
                this.customerService.getCustomers();
        return ResponseEntity.ok(customerList.get());
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Long> addNewCustomer(@RequestBody CustomerDto customerDto) {

        Long customerId = this.customerService.saveCustomer(customerDto);
        return ResponseEntity
                .created(URI.create("/customer/api/v1/" + customerId.longValue()))
                .build();
    }

    @PutMapping("/{customerId}")
    public ResponseEntity updateCustomer(@PathVariable("customerId") Long customerId,
                                         @RequestBody CustomerDto customerDto) throws CustomerNotFoundException {

        this.customerService.updateCustomer(customerId, customerDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable("customerId") Long customerId) {
        this.customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
