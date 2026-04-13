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
@Table(name = "tbl_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;
    @Column(name = "pro_name", nullable = false, columnDefinition = "CHAR(255)")
    private String name;
    @Column( name = "pro_price",nullable = false, precision = 25, scale = 4)
    private BigDecimal price;

    @Column( name = "pro_image",length = 255, columnDefinition = "VARCHAR(255) DEFAULT 'no_image.png'")
    private String image = "no_image.png";

    @Column( name = "pro_cost",precision = 25, scale = 4)
    private BigDecimal cost;

    @Column(name = "pro_type",  nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'standard'")
    private String type = "standard";

    @Column( name = "pro_details",columnDefinition = "TEXT")
    private String details;

    @Column(name = "pro_alert_quantity", precision = 10, scale = 4, columnDefinition = "NUMERIC(10,4) DEFAULT 0")
    private BigDecimal alertQuantity = BigDecimal.ZERO;

    @Column(name = "pro_show_flag", columnDefinition = "SMALLINT DEFAULT 0")
    private Integer showFlag = 0;
    @Column(name = "pro_status", length = 10)
    private GeneralStatus status = GeneralStatus.ACTIVE;
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