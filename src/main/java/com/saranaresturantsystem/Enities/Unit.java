package com.saranaresturantsystem.Enities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_units")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_unit")
    private Integer baseUnit;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "operation", length = 5)
    private String operation; // e.g., "*", "/"

    @Column(name = "operation_value", precision = 10, scale = 2)
    private BigDecimal operationValue;

    @Column(name = "delete_flag", columnDefinition = "SMALLINT DEFAULT 0")
    private Integer deleteFlag = 0;
}