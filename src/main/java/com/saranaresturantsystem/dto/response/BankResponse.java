package com.saranaresturantsystem.dto.response;

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
    private String  isDefault;
    private String statement;
    private  String status ;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}
