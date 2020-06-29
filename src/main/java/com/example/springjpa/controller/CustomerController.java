package com.example.springjpa.controller;

import com.example.springjpa.model.CustomerDto;
import com.example.springjpa.exceptions.CustomerNotFoundException;
import com.example.springjpa.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customer/api/v1")
@Validated // for enabling method valid validation
public class CustomerController {

    @Value("${log.level}")
    private String logLevel;

    @Value("${hello.message}")
    private String message;

    @Value("${spring.datasource.password}")
    private String password;

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

    @GetMapping("/customers")
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
                .created(URI.create("/customer/api/v1/" + customerId.longValue()))
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

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> cloudConfigMap() {
        Map<String, String> map = new HashMap<>();
        map.put("log.level", logLevel);
        map.put("message", message);
        map.put("db pass" , password);

        return ResponseEntity.ok(map);
    }
}
