package com.saranaresturantsystem.entities;

import com.saranaresturantsystem.entities.status.GeneralStatus;
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

    @Column(name = "unit_id")
    private Long id;

    @Column(name = "un_base_unit")
    private Integer baseUnit;

    @Column(name = "un_code", length = 50)
    private String code;

    @Column(name = "un_name", length = 50)
    private String name;

    @Column(name = "un_operation", length = 5)
    private String operation; // e.g., "*", "/"

    @Column(name = "un_operation_value", precision = 10, scale = 2)
    private BigDecimal operationValue;

    @Column(name = "un_delete_flag", nullable = false)
    private Short deleteFlag = 0;
    @Enumerated(EnumType.STRING)
    @Column(name = "un_status", length = 10)
    private GeneralStatus status = GeneralStatus.ACTIVE ;
}