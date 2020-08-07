package com.rapidkart.customerservice.service;

import com.rapidkart.customerservice.config.JmsConfig;
import com.rapidkart.customerservice.domain.Customer;
import com.rapidkart.customerservice.mapper.CustomerMapper;
import com.rapidkart.customerservice.repository.CustomerRepository;
import com.rapidkart.model.events.ValidateOrderRequest;
import com.rapidkart.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ValidateCustomerListener {

    private final JmsTemplate jmsTemplate;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @JmsListener(destination = JmsConfig.CUSTOMER_VALIDATION_QUEUE)
    public void listen(ValidateOrderRequest request) {
        // check if the customer is legitimate or not
        // send response to a new response queue accordingly

        Optional<Customer> customerOptional =
                customerRepository.findByCustomerId(request.getCustomerId());

        ValidateOrderResult result = null;

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            boolean isLegitimate = customer.getIsLegitimateUser();

            result = ValidateOrderResult.builder()
                    .customerDto(customerMapper.customerToCustomerDto(customer))
                    .customerExists(true)
                    .build();
        } else {
            result = ValidateOrderResult.builder()
                        .customerExists(false)
                        .build();
        }
        jmsTemplate.convertAndSend(JmsConfig.CUSTOMER_VALIDATION_RESPONSE_QUEUE, result);
    }
}
