package com.saranaresturantsystem.entities.product;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column( length = 10 )
    private StatusType status ;
    @OneToMany(mappedBy = "unit")
    private List<Product> products;

}