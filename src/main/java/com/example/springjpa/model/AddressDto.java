package com.example.springjpa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto {

    private Long id;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private AddressType addressType;
}
