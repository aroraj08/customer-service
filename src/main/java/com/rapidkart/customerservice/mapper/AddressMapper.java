package com.rapidkart.customerservice.mapper;

import com.rapidkart.customerservice.domain.Address;
import com.rapidkart.customerservice.model.AddressDto;
import org.mapstruct.Mapper;

@Mapper
public interface AddressMapper {

    Address addressDtoToAddress(AddressDto addressDto);

    AddressDto addressToAddressDto(Address address);

}
