package com.saranaresturantsystem.entities;


import com.saranaresturantsystem.entities.status.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;
    private String name;
    private BigDecimal salePrice;
    @Column(length = 500)
    private String image ;
    private BigDecimal costPrice;
    private String type;
    private String details;
    private BigDecimal alertQuantity = BigDecimal.ZERO;
    @Column(name = "default_sale_unit")
    private Integer defaultSaleUnit;
    @Column(name = "default_purchase_unit")
    private Integer defaultPurchaseUnit;
    @Column(name = "printer")
    private Integer printer;

    @Enumerated(EnumType.STRING)
    @Column( length = 10 )
    private StatusType status ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory section;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;
    // Unit helpers for Sales/Purchase

}