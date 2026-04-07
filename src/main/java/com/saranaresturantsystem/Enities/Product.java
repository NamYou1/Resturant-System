package com.saranaresturantsystem.Enities;

import jakarta.persistence.*;
import lombok.*;
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
    @Column(nullable = false, columnDefinition = "CHAR(255)")
    private String name;
    @Column(nullable = false, precision = 25, scale = 4)
    private BigDecimal price;

    @Column(length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'no_image.png'")
    private String image = "no_image.png";

    @Column(precision = 25, scale = 4)
    private BigDecimal cost;

    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'standard'")
    private String type = "standard";

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "alert_quantity", precision = 10, scale = 4, columnDefinition = "NUMERIC(10,4) DEFAULT 0")
    private BigDecimal alertQuantity = BigDecimal.ZERO;

    @Column(name = "show_flag", columnDefinition = "SMALLINT DEFAULT 0")
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