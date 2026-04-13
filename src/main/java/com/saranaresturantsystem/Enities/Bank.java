package com.saranaresturantsystem.Enities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50 , unique = true , nullable = false)
    private String name;
    @Column(length = 50 , unique = true , nullable = false)
    private String number;
    @Column(length = 50)
    private String amount;
    @Column(length = 50)
    private String is_default;
    @Column(length = 100)
    private String statement;
    @Column (length = 50)
    private LocalDate fromTime;
    private LocalDate toTime;
    @Column(length = 5 )
    private String status;
}
