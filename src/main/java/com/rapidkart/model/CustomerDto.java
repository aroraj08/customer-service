package com.rapidkart.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@Getter
@Setter
public class CustomerDto implements Serializable {

    @JsonProperty("customerId")
    @Null
    private Long customerId;

    @JsonProperty("FirstName")
    @NotBlank
    private String firstName;

    @JsonProperty("LastName")
    @NotBlank
    private String lastName;

    @JsonProperty("addresses")
    private Set<AddressDto> addressSet;

    @JsonProperty("isLegitimateUser")
    private Boolean isLegitimateUser;
}
