package com.rapidkart.model.events;

import com.rapidkart.model.CustomerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class ValidateOrderResult {

    private CustomerDto customerDto;
    private Boolean customerExists = true;
}
