package com.saranaresturantsystem.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_sale_items")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;
    @Column(name = "product_id", nullable = false)
    private Integer productId;
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantity;
    @Column(name = "unit_price", nullable = false, precision = 25, scale = 4)
    private BigDecimal unitPrice;
    @Column(name = "net_unit_price", nullable = false, precision = 25, scale = 4)
    private BigDecimal netUnitPrice;
    @Column(length = 20)
    private String discount;
    @Column(name = "item_discount", precision = 25, scale = 4)
    private BigDecimal itemDiscount;
    private Integer tax;
    @Column(name = "item_tax", precision = 25, scale = 4)
    private BigDecimal itemTax;
    @Column(nullable = false, precision = 25, scale = 4)
    private BigDecimal subtotal;
    @Column(name = "real_unit_price", precision = 25, scale = 4)
    private BigDecimal realUnitPrice;
    @Column(precision = 25, scale = 4)
    private BigDecimal cost = BigDecimal.ZERO;
    @Column(name = "product_code", length = 50)
    private String productCode;
    @Column(name = "product_name", length = 50)
    private String productName;
    @Column(length = 255)
    private String comment;
    @Column(name = "product_unit")
    private Integer productUnit;
    @Column(name = "unit_quantity", precision = 12, scale = 2)
    private BigDecimal unitQuantity;
    @Column(name = "sale_unit")
    private Integer saleUnit;
    @Column(name = "modify_name", length = 255)
    private String modifyName;
    @Column(name = "modify_id", length = 100)
    private String modifyId;
    @Column(name = "modify_total", precision = 14, scale = 4)
    private BigDecimal modifyTotal;
    @Column(name = "quantity_print", precision = 12, scale = 2)
    private BigDecimal quantityPrint;
    @Column(name = "sale_type", length = 50)
    private String saleType;
}