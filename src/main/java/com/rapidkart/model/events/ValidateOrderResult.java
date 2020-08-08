package com.rapidkart.model.events;

import com.rapidkart.model.CustomerDto;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateOrderResult {

    private Long orderId;
    private Boolean isValidUser;
}
