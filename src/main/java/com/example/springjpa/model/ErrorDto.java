package com.example.springjpa.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ErrorDto {

    private String errorMessage;
    private String errorCode;
    private HttpStatus status;
    private OffsetDateTime time;

}
