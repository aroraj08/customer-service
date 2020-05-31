package com.example.springjpa.mapper;

import com.example.springjpa.domain.Address;
import com.example.springjpa.model.AddressDto;
import com.example.springjpa.model.CustomerDto;
import com.example.springjpa.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);

    //@Mapping(target = "addressSet", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
