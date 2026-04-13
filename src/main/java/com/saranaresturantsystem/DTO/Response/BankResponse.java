package com.saranaresturantsystem.DTO.Response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BankResponse {
    private Long id;
    private String name;
    private String number;
    private String amount;
    private String  is_default;
    private String statement;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}
