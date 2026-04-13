package com.saranaresturantsystem.Enities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_purchase_items")
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantity;
    @Column(name = "total_discount")
    private BigDecimal totalDiscount;
    @Column(nullable = false, precision = 25, scale = 4)
    private BigDecimal cost;
    @Column(nullable = false, precision = 25, scale = 4)
    private BigDecimal subtotal; // (cost * quantity) - item_discount
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_unit")
    private Unit unit;
    @Column(name = "real_unit_cost")
    private BigDecimal realUnitCost;
    @Column(name = "store_id")
    private Integer storeId;
}