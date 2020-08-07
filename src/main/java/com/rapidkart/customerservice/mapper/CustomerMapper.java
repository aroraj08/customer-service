package com.rapidkart.customerservice.mapper;

import com.rapidkart.model.CustomerDto;
import com.rapidkart.customerservice.domain.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);

    //@Mapping(target = "addressSet", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
