package com.saranaresturantsystem.entities;


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
    private BigDecimal price;

    private String image = "no_image.png";

    private BigDecimal cost;

    private String type = "standard";

    private String details;

    private BigDecimal alertQuantity = BigDecimal.ZERO;

    private Integer showFlag = 0;
    // Relationships to your existing Entities
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section")
    private SubCategory section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit")
    private Unit unit;

    // Unit helpers for Sales/Purchase
    @Column(name = "default_sale_unit")
    private Integer defaultSaleUnit;

    @Column(name = "default_purchase_unit")
    private Integer defaultPurchaseUnit;

    @Column(name = "printer")
    private Integer printer;
}